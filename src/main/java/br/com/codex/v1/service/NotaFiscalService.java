package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.mapper.NotaFiscalMapper;
import br.com.codex.v1.utilitario.Base64Util;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.Assinar;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.Validar;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.exception.NfeValidacaoException;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;

import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.util.CancelamentoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import br.com.swconsultoria.nfe.util.ConstantesUtil;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import br.com.codex.v1.domain.repository.NotaFiscalDuplicatasRepository;
import br.com.codex.v1.domain.repository.NotaFiscalItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotaFiscalService {

    String host = null;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Autowired
    private NotaFiscalDuplicatasRepository notaFiscalDuplicatasRepository;

    @Autowired
    private NotaFiscalItemRepository notaFiscalItemRepository;

    @Autowired
    private ConfiguracaoCertificadoRepository certificadoRepository;

    private static final String CAMINHO_SCHEMAS = "src/main/resources/schemas";

    public ConfiguracoesNfe iniciarConfiguracao(NotaFiscalDto notaFiscalDto) throws Exception {
        Optional<ConfiguracaoCertificado> cert = certificadoRepository.findByCnpj(notaFiscalDto.getCnpjEmit());

        String senhaDecodificada = Base64Util.decode(cert.get().getSenha());

        Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);
        return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.valueOf(cert.get().getUf()), AmbienteEnum.HOMOLOGACAO, certificado,"schemas");
    }

    public TEnviNFe montarNotaFiscal(NotaFiscal nota) throws Exception {
        TNFe tnfe = NotaFiscalMapper.paraTNFe(nota);

        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setIdLote("001");
        enviNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        enviNFe.setIndSinc("1");
        enviNFe.getNFe().add(tnfe);
        return enviNFe;
    }

    public TEnviNFe assinarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws Exception {
        String xml = XmlNfeUtil.objectToXml(enviNFe);
        String xmlAssinado = Assinar.assinaNfe(configuracoes, xml, AssinaturaEnum.NFE);  // <--- Usar enum
        return XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
    }

    public void validarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException, JAXBException {
        String xml = XmlNfeUtil.objectToXml(enviNFe);
        Validar validador = new Validar();

        boolean valido = validador.isValidXml(configuracoes, xml, ServicosEnum.ENVIO);
        if (!valido) {
            throw new NfeValidacaoException("Erro na validação do XML da Nota Fiscal.");
        }
    }

    public TRetEnviNFe enviarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException {
        return Nfe.enviarNfe(configuracoes, enviNFe, DocumentoEnum.NFE);
    }

    public void salvarXmlNotaFiscal(String chaveAcesso, String xml) throws IOException {

        host = extrairHostDoJdbcUrl(datasourceUrl);
        String CAMINHO_SAIDA_XML = "//" + host + "/tmp/xml/saidas/";

        Path caminhoArquivo = Paths.get(CAMINHO_SAIDA_XML + chaveAcesso + "-nfe.xml");
        Files.createDirectories(caminhoArquivo.getParent());
        Files.writeString(caminhoArquivo, xml, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void cancelarNotaFiscal(String chaveAcesso, String protocolo, String motivo, String cnpjEmitente, ConfiguracoesNfe config) throws Exception {
        // Monta os dados do evento de cancelamento
        Evento evento = new Evento();
        evento.setChave(chaveAcesso);
        evento.setProtocolo(protocolo);
        evento.setCnpj(cnpjEmitente);
        evento.setMotivo(motivo);
        evento.setDataEvento(LocalDateTime.now());

        // Gera o objeto TEnvEvento
        TEnvEvento eventoCancelamento = CancelamentoUtil.montaCancelamento(evento, config);

        // Assina o XML do evento
        String xmlEvento = XmlNfeUtil.objectToXml(eventoCancelamento, config.getEncode());
        String xmlEventoAssinado = Assinar.assinaNfe(config, xmlEvento, AssinaturaEnum.EVENTO);

        // Reconverte o XML assinado em objeto TEnvEvento
        eventoCancelamento = XmlNfeUtil.xmlToObject(xmlEventoAssinado, TEnvEvento.class);

        // Envia o evento para a SEFAZ
        TRetEnvEvento retorno = Nfe.cancelarNfe(config, eventoCancelamento, true, DocumentoEnum.NFE);

        // Gera o XML do cancelamento processado
        String xmlCancelado = CancelamentoUtil.criaProcEventoCancelamento(config, eventoCancelamento, retorno.getRetEvento().get(0));

        // Salva o XML processado (implementação personalizada sua)
        salvarXmlNotaFiscal(chaveAcesso + "-cancelamento", xmlCancelado);
    }

    private String extrairHostDoJdbcUrl(String jdbcUrl) {
        // Exemplo: jdbc:mysql://vps55189.publiccloud.com.br:3306/codex
        try {
            URI uri = new URI(jdbcUrl.replaceFirst("^jdbc:", ""));
            return uri.getHost();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair host da URL do banco de dados", e);
        }
    }
}
