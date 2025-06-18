package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.contabilidade.ControleNsu;
import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import br.com.codex.v1.domain.repository.ControleNsuRepository;
import br.com.codex.v1.domain.repository.XmlNotaFiscalRepository;
import br.com.codex.v1.utilitario.Base64Util;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ControleNsuService {
    private static final Logger logger = LoggerFactory.getLogger(ControleNsuService.class);

    @Autowired
    private ControleNsuRepository controleNsuRepository;

    @Autowired
    private ConfiguracaoCertificadoRepository certificadoRepository;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Autowired
    private XmlNotaFiscalRepository xmlNotaFiscalRepository;

    /**
     * Consulta o último NSU registrado para um CNPJ.
     */
    public BigInteger consultarUltimoNSU(String cnpj) {
        logger.info("Consultando último NSU para CNPJ: {}", cnpj);
        return controleNsuRepository.findUltimoNsuByCnpj(cnpj)
                .map(BigInteger::valueOf)
                .orElse(BigInteger.ZERO);
    }

    /**
     * Consulta documentos fiscais na SEFAZ e atualiza o controle de NSU.
     */
    @Transactional
    public void consultarDocumentos(String cnpj, String ambiente) throws NfeException {
        logger.info("Consultando documentos para CNPJ: {}, Ambiente: {}", cnpj, ambiente);

        // Busca o controle de NSU
        Optional<ControleNsu> controleNsuOpt = controleNsuRepository.findByCnpjAndAmbiente(cnpj, ambiente);
        ControleNsu controleNsu = controleNsuOpt.orElseGet(() -> {
            ControleNsu novo = new ControleNsu();
            novo.setCnpj(cnpj);
            novo.setAmbiente(ambiente);
            novo.setUltimoNsu(0L);
            novo.setDataUltimaConsulta(LocalDateTime.now());
            return novo;
        });

        // Configurações da SEFAZ
        ConfiguracoesNfe config = obterConfiguracoesNfe(cnpj);

        String nsu = String.format("%015d", controleNsu.getUltimoNsu());

        // Consulta a SEFAZ
        RetDistDFeInt retorno = Nfe.distribuicaoDfe(config, PessoaEnum.JURIDICA, cnpj, ConsultaDFeEnum.NSU, nsu);

        // Atualiza o NSU
        BigInteger maxNsu = new BigInteger(retorno.getUltNSU());
        controleNsu.setUltimoNsu(maxNsu.longValue());
        controleNsu.setDataUltimaConsulta(LocalDateTime.now());
        controleNsuRepository.save(controleNsu);

        // Salva os documentos retornados
        if (retorno.getLoteDistDFeInt() != null) {
            for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
                String xmlContent = new String(doc.getValue());
                String chave = extrairChaveDoXml(xmlContent);
                salvarXmlNotaFiscal(chave, xmlContent);
            }
        }
    }

    private ConfiguracoesNfe obterConfiguracoesNfe(String cnpj) throws NfeException {
        Optional<ConfiguracaoCertificado> cert = certificadoRepository.findByCnpj(cnpj);
        Optional<AmbienteNotaFiscal> ambienteNotaFiscal = ambienteNotaFiscalRepository.findById(1L);

        if (ambienteNotaFiscal.isEmpty()) {
            throw new NfeException("O ambiente da nota fiscal não está parametrizado");
        }

        if (cert.isEmpty()) {
            throw new NfeException("Certificado não encontrado para CNPJ: " + cnpj);
        }

        try {
            String senhaDecodificada = Base64Util.decode(cert.get().getSenha());
            Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);
            return ConfiguracoesNfe.criarConfiguracoes(
                    EstadosEnum.valueOf(cert.get().getUf()),
                    AmbienteEnum.valueOf(String.valueOf(ambienteNotaFiscal.get().getCodigoAmbiente())),
                    certificado,
                    "schemas"
            );
        } catch (Exception e) {
            logger.error("Erro ao configurar certificado", e);
            throw new NfeException("Falha na configuração do certificado", e);
        }
    }

    @Transactional
    public void salvarXmlNotaFiscal(String chaveAcesso, String xml) throws NfeException {
        try {
            XmlNotaFiscal xmlNotaFiscal = new XmlNotaFiscal();
            xmlNotaFiscal.setChaveAcesso(chaveAcesso);
            xmlNotaFiscal.setXmlContent(xml);
            xmlNotaFiscal.setDataCriacao(LocalDateTime.now());
            xmlNotaFiscal.setTipoDocumento(DocumentoEnum.NFE.getTipo());
            xmlNotaFiscalRepository.save(xmlNotaFiscal);
            logger.info("XML salvo com sucesso para a chave: {}", chaveAcesso);
        } catch (Exception e) {
            throw new NfeException("Erro ao salvar XML no banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Métudo auxiliar para extrair a chave do XML
     */
    private String extrairChaveDoXml(String xmlContent) {
        try {
            // Extrai a chave do XML (exemplo: busca por <chNFe> ou <chave>)
            int startIndex = xmlContent.indexOf("<chNFe>") + 7;
            if (startIndex < 7) {
                startIndex = xmlContent.indexOf("<chave>") + 7;
            }
            if (startIndex >= 7) {
                int endIndex = xmlContent.indexOf("<", startIndex);
                if (endIndex > startIndex) {
                    return xmlContent.substring(startIndex, endIndex);
                }
            }
        } catch (Exception e) {
            logger.warn("Não foi possível extrair a chave do XML", e);
        }
        // Fallback: gera um identificador único
        return "doc-" + System.currentTimeMillis();
    }
}