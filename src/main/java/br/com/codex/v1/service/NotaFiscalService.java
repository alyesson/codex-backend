package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import br.com.codex.v1.domain.contabilidade.SerieNfe;
import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.domain.ti.Atendimentos;
import br.com.codex.v1.mapper.NotaFiscalMapper;
import br.com.codex.v1.utilitario.Base64Util;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.Assinar;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.Validar;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TInutNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.util.CancelamentoUtil;
import br.com.swconsultoria.nfe.util.CartaCorrecaoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import br.com.swconsultoria.nfe.util.ConstantesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.util.stream.Collectors;

@Service
public class NotaFiscalService {
    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalService.class);

    @Autowired
    private ConfiguracaoCertificadoRepository certificadoRepository;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Autowired
    private SerieNfeRepository serieNfeRepository;

    @Autowired
    private XmlNotaFiscalRepository xmlNotaFiscalRepository;

    @Autowired
    private ControleNsuService controleNsuService;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    Integer ambienteNota;

    /**
     * Executa o processo completo de emissão da NF-e.
     */
    @Transactional
    public NotaFiscalDto emitirNotaFiscal(NotaFiscalDto dto) throws Exception {
        logger.info("Iniciando emissão da NF-e para CNPJ: {}", dto.getDocumentoEmitente());
        ConfiguracoesNfe config = iniciarConfiguracao(dto);
        TEnviNFe enviNFe = montarNotaFiscal(dto);
        enviNFe = assinarNotaFiscal(enviNFe, config);
        validarNotaFiscal(enviNFe, config);
        TRetEnviNFe retorno = transmiteNotaFiscal(enviNFe, config);

        dto.setChave(retorno.getProtNFe().getInfProt().getChNFe());
        dto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
        dto.setNumeroProtocolo(retorno.getProtNFe().getInfProt().getNProt());
        dto.setMotivoProtocolo(retorno.getProtNFe().getInfProt().getXMotivo());

        String xml = XmlNfeUtil.objectToXml(enviNFe, config.getEncode());
        salvarXmlNotaFiscal(dto.getChave(), xml);

        return dto;
    }

    /**
     * Inicializa as configurações da NF-e com certificado e ambiente.
     */
    public ConfiguracoesNfe iniciarConfiguracao(NotaFiscalDto notaFiscalDto) throws NfeException {
        logger.info("Iniciando configuração para NF-e, CNPJ: {}", notaFiscalDto.getDocumentoEmitente());
        Optional<ConfiguracaoCertificado> cert = certificadoRepository.findByCnpj(notaFiscalDto.getDocumentoEmitente());
        Optional<AmbienteNotaFiscal> ambienteNotaFiscal = ambienteNotaFiscalRepository.findById(1L);

        if (ambienteNotaFiscal.isPresent()) {
            ambienteNota = ambienteNotaFiscal.get().getCodigoAmbiente();;
        } else {
            throw new NfeException("O ambiente da nota fiscal não está parametrizado");
        }

        if (cert.isEmpty()) {
            throw new NfeException("Certificado não encontrado para CNPJ: " + notaFiscalDto.getDocumentoEmitente());
        }

        try {
            String senhaDecodificada = Base64Util.decode(cert.get().getSenha());
            Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);
            return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.valueOf(cert.get().getUf()),
                    AmbienteEnum.valueOf(String.valueOf(ambienteNotaFiscal.get().getCodigoAmbiente())),
                    certificado, "schemas"
            );
        } catch (Exception e) {
            logger.error("Erro ao configurar certificado", e);
            throw new NfeException("Falha na configuração do certificado", e);
        }
    }

    /**
     * Monta a estrutura da NF-e a partir do DTO, validando e atualizando a série.
     */
    @Transactional
    public TEnviNFe montarNotaFiscal(NotaFiscalDto nota) throws NfeException {
        logger.info("Montando NF-e para número: {} série: {}", nota.getNumero(), nota.getSerie());

        // Valida e atualiza a série
        Optional<SerieNfe> serieOpt = serieNfeRepository.findByNumeroSerieAndCnpjAndAmbiente(nota.getSerie(), nota.getDocumentoEmitente(), String.valueOf(ambienteNota));
        SerieNfe serie;
        if (serieOpt.isEmpty()) {
            serie = new SerieNfe();
            serie.setNumeroSerie(nota.getSerie());
            serie.setCnpj(nota.getDocumentoEmitente());
            serie.setAmbiente(String.valueOf(ambienteNota));
            serie.setTipoDocumento(DocumentoEnum.NFE.name());
            serie.setUltimoNumero(Integer.valueOf(nota.getNumero()));
            serie.setStatus("Ativo");
            serie.setDataCriacao(LocalDateTime.now());
        } else {
            serie = serieOpt.get();
            if (Integer.parseInt(nota.getNumero()) <= serie.getUltimoNumero()) {
                throw new NfeException("Número da NF-e já utilizado para a série: " + nota.getSerie());
            }
            serie.setUltimoNumero(Integer.valueOf(nota.getNumero()));
        }
        serieNfeRepository.save(serie);

        TNFe tnfe = NotaFiscalMapper.paraTNFe(nota);
        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setIdLote(String.format("%015d", System.currentTimeMillis()));
        enviNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        enviNFe.setIndSinc("1"); // Processamento síncrono
        enviNFe.getNFe().add(tnfe);
        return enviNFe;
    }

    /**
     * Assina o XML da NF-e.
     */
    public TEnviNFe assinarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException, JAXBException {
        logger.info("Assinando NF-e, ID Lote: {}", enviNFe.getIdLote());
        String xml = XmlNfeUtil.objectToXml(enviNFe, configuracoes.getEncode());
        String xmlAssinado = Assinar.assinaNfe(configuracoes, xml, AssinaturaEnum.NFE);
        return XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
    }

    /**
     * Valida o XML da NF-e contra os schemas da SEFAZ.
     */
    public void validarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException, JAXBException {
        logger.info("Validando NF-e, ID Lote: {}", enviNFe.getIdLote());
        String xml = XmlNfeUtil.objectToXml(enviNFe, configuracoes.getEncode());
        String xmlAssinado = Assinar.assinaNfe(configuracoes, xml, AssinaturaEnum.NFE);
        XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
    }

    /**
     * Transmite a NF-e para a SEFAZ.
     */
    public TRetEnviNFe transmiteNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException {
        logger.info("Enviando NF-e, ID Lote: {}", enviNFe.getIdLote());
        TRetEnviNFe retorno = Nfe.enviarNfe(configuracoes, enviNFe, DocumentoEnum.NFE);
        if (!"100".equals(retorno.getProtNFe().getInfProt().getCStat())) {
            throw new NfeException("Erro ao enviar NF-e: " + retorno.getProtNFe().getInfProt().getXMotivo());
        }
        return retorno;
    }

    /**
     * Salva o XML da NF-e no banco de dados.
     */
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
     * Cancela uma NF-e.
     */
    @Transactional
    public TRetEnvEvento cancelarNotaFiscal(String chave, String protocolo, String motivo, String cnpj, ConfiguracoesNfe config) throws NfeException, JAXBException {
        logger.info("Cancelando NF-e, chave: {}", chave);

        // Valida parâmetros
        if (chave == null || chave.length() != 44) {
            throw new NfeException("Chave de acesso inválida: " + chave);
        }
        if (protocolo == null || protocolo.trim().isEmpty()) {
            throw new NfeException("Protocolo inválido: " + protocolo);
        }
        if (motivo == null || motivo.trim().isEmpty() || motivo.length() > 255) {
            throw new NfeException("Motivo inválido: deve ter entre 1 e 255 caracteres");
        }
        if (cnpj == null || cnpj.length() < 14) {
            throw new NfeException("CNPJ inválido: " + cnpj);
        }

        // Cria o objeto Evento
        Evento cancelamento = new Evento();
        cancelamento.setChave(chave);
        cancelamento.setProtocolo(protocolo);
        cancelamento.setMotivo(motivo);
        cancelamento.setCnpj(cnpj);
        cancelamento.setDataEvento(LocalDateTime.now());

        // Monta o evento de cancelamento
        TEnvEvento envEvento = CancelamentoUtil.montaCancelamento(cancelamento, config);

        // Assina o evento
        String xmlEvento = XmlNfeUtil.objectToXml(envEvento, config.getEncode());
        xmlEvento = Assinar.assinaNfe(config, xmlEvento, AssinaturaEnum.EVENTO);

        // Valida o XML
        if (!new Validar().isValidXml(config, xmlEvento, ServicosEnum.CANCELAMENTO)) {
            logger.error("XML inválido para cancelamento: {}", xmlEvento);
            throw new NfeException("Erro na validação do XML de cancelamento");
        }

        // Envia o evento
        TRetEnvEvento retorno = Nfe.cancelarNfe(config, envEvento, true, DocumentoEnum.NFE);

        // Verifica o retorno
        if (!"135".equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
            throw new NfeException("Erro ao cancelar NF-e: " + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
        }

        // Gera e salva o procEventoNFe
        String xmlProcEvento = CancelamentoUtil.criaProcEventoCancelamento(config, envEvento, retorno.getRetEvento().get(0));
        salvarXmlNotaFiscal(retorno.getRetEvento().get(0).getInfEvento().getId(), xmlProcEvento);

        return retorno;
    }

    /**
     * Emite uma Carta de Correção Eletrônica (CC-e) para uma NF-e.
     */
    @Transactional
    public br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento cartaCorrecao(String chave, String cnpj, String correcao, ConfiguracoesNfe config) throws NfeException, JAXBException {
        logger.info("Emitindo Carta de Correção para NF-e, chave: {}", chave);

        // Valida parâmetros
        if (chave == null || chave.length() != 44) {
            throw new NfeException("Chave de acesso inválida: " + chave);
        }
        if (cnpj == null || cnpj.length() < 14) {
            throw new NfeException("CNPJ inválido: " + cnpj);
        }
        if (correcao == null || correcao.trim().isEmpty() || correcao.length() > 1000) {
            throw new NfeException("Correção inválida: deve ter entre 1 e 1000 caracteres");
        }

        // Determina o número sequencial
        String sequencia = determinarProximoSequencial(chave);

        // Cria o objeto Evento
        Evento cce = new Evento();
        cce.setChave(chave);
        cce.setCnpj(cnpj);
        cce.setMotivo(correcao);
        cce.setSequencia(Integer.parseInt(sequencia));
        cce.setDataEvento(LocalDateTime.now());

        // Monta o evento de CC-e
        br.com.swconsultoria.nfe.schema.envcce.TEnvEvento envEvento = CartaCorrecaoUtil.montaCCe(cce, config);

        // Corrige cOrgao para 91 (RFB)
        envEvento.getEvento().forEach(evento -> evento.getInfEvento().setCOrgao("91"));

        // Converte para XML e assina (replicando a lógica de CartaCorrecao.eventoCCe())
        String xml = XmlNfeUtil.objectToXml(envEvento, config.getEncode());
        xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

        xml = Assinar.assinaNfe(config, xml, AssinaturaEnum.EVENTO);

        // Valida o XML
        if (!new Validar().isValidXml(config, xml, ServicosEnum.CCE)) {
            logger.error("XML inválido para CC-e: {}", xml);
            throw new NfeException("Erro na validação do XML de Carta de Correção");
        }

        // Envia o evento usando a classe Nfe (que deve ser pública)
        // Esta é a forma correta de enviar eventos segundo a API
        br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento retorno = Nfe.cce(config, envEvento, true);

        // Verifica o retorno
        if (!"135".equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
            throw new NfeException("Erro ao emitir Carta de Correção: " + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
        }

        // Gera e salva o procEventoNFe
        String xmlProcEvento = CartaCorrecaoUtil.criaProcEventoCCe(config, envEvento, retorno);
        salvarXmlNotaFiscal(retorno.getRetEvento().get(0).getInfEvento().getId(), xmlProcEvento);

        return retorno;
    }

    /**
     * Inutiliza uma faixa de numeração de NF-e.
     */
    @Transactional
    public TRetInutNFe inutilizarNotaFiscal(String cnpj, String justificativa, String ano, String serie, String numInicial, String numFinal, ConfiguracoesNfe config) throws NfeException, JAXBException {
        logger.info("Inutilizando faixa de numeração para NF-e, série: {}, numInicial: {}, numFinal: {}", serie, numInicial, numFinal);
        TInutNFe inutNFe = new TInutNFe();
        inutNFe.setVersao(ConstantesUtil.VERSAO.NFE);

        TInutNFe.InfInut infInut = new TInutNFe.InfInut();
        infInut.setId("ID" + config.getEstado().getCodigoUF() + cnpj + "55" + serie + numInicial + numFinal);
        infInut.setTpAmb(config.getAmbiente().getCodigo());
        infInut.setXServ("INUTILIZAR");
        infInut.setCNPJ(cnpj);
        infInut.setMod("55"); // NF-e
        infInut.setSerie(serie);
        infInut.setNNFIni(numInicial);
        infInut.setNNFFin(numFinal);
        infInut.setXJust(justificativa);
        infInut.setAno(ano);
        inutNFe.setInfInut(infInut);

        TRetInutNFe retorno = Nfe.inutilizacao(config, inutNFe, DocumentoEnum.NFE, true);

        if (!"102".equals(retorno.getInfInut().getCStat())) {
            throw new NfeException("Erro ao inutilizar NF-e: " + retorno.getInfInut().getXMotivo());
        }

        String xml = XmlNfeUtil.objectToXml(retorno, config.getEncode());
        salvarXmlNotaFiscal(infInut.getId(), xml);

        return retorno;
    }

    /**
     * Consulta o status de serviço da SEFAZ.
     */
    public TRetConsStatServ consultarStatusServico(ConfiguracoesNfe config) throws NfeException {
        logger.info("Consultando status de serviço da SEFAZ");
        return Nfe.statusServico(config, DocumentoEnum.NFE);
    }

    /**
     * Consulta o status de uma NF-e.
     */
    public br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TRetConsSitNFe consultarNotaFiscal(String chave, ConfiguracoesNfe config) throws NfeException {
        logger.info("Consultando status da NF-e, chave: {}", chave);
        return Nfe.consultaXml(config, chave, DocumentoEnum.NFE);
    }

    /**
     * Envia um evento manual (ex.: manifestação do destinatário, EPEC).
     */
    @Transactional
    public String enviarEventoManual(String xmlEvento, ServicosEnum tipoEvento, boolean valida, boolean assina, ConfiguracoesNfe config) throws NfeException {
        logger.info("Enviando evento manual, tipo: {}", tipoEvento);

        // Validação do XML se necessário
        if (valida) {
            if (!new Validar().isValidXml(config, xmlEvento, tipoEvento)) {
                throw new NfeException("XML do evento inválido para validação");
            }
        }

        // Assinatura do XML se necessário
        String xmlParaEnvio = xmlEvento;
        if (assina) {
            xmlParaEnvio = Assinar.assinaNfe(config, xmlEvento, AssinaturaEnum.EVENTO);
        }

        // Envio do evento de acordo com o tipo
        String xmlRetorno;
        try {
            if (tipoEvento == ServicosEnum.CCE) {
                // Para Carta de Correção
                br.com.swconsultoria.nfe.schema.envcce.TEnvEvento envCCe =
                        XmlNfeUtil.xmlToObject(xmlParaEnvio, br.com.swconsultoria.nfe.schema.envcce.TEnvEvento.class);
                br.com.swconsultoria.nfe.schema.envcce.TRetEnvEvento retCCe = Nfe.cce(config, envCCe, false);
                xmlRetorno = XmlNfeUtil.objectToXml(retCCe, config.getEncode());
            }
            else if (tipoEvento == ServicosEnum.CANCELAMENTO) {
                // Para Cancelamento
                br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento envCanc =
                        XmlNfeUtil.xmlToObject(xmlParaEnvio, br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento.class);
                // Agora usando o método correto com 4 parâmetros
                br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento retCanc =
                        Nfe.cancelarNfe(config, envCanc, false, DocumentoEnum.NFE);
                xmlRetorno = XmlNfeUtil.objectToXml(retCanc, config.getEncode());
            }
            else {
                // Para outros tipos de evento, usar o método genérico
                xmlRetorno = Nfe.enviarEnventoManual(config, xmlParaEnvio, tipoEvento, false, false, DocumentoEnum.NFE);
            }
        } catch (JAXBException e) {
            throw new NfeException("Erro ao processar XML do evento: " + e.getMessage(), e);
        }

        // Salva o XML de retorno
        String chaveEvento = xmlRetorno.contains("chNFe") ? extrairChaveEvento(xmlRetorno) : "evento-" + System.currentTimeMillis();
        salvarXmlNotaFiscal(chaveEvento, xmlRetorno);

        return xmlRetorno;
    }

    /**
     * Consulta e atualiza a sequência de NSU, integrando com ControleNsuService.
     */
    @Transactional
    public BigInteger consultarNSU(String cnpj, String ambiente) throws NfeException {
        logger.info("Consultando NSU para CNPJ: {}, Ambiente: {}", cnpj, ambiente);
        controleNsuService.consultarDocumentos(cnpj, ambiente);
        return controleNsuService.consultarUltimoNSU(cnpj);
    }

    /**
     * Extrai a chave de evento do XML (métudo auxiliar).
     */
    private String extrairChaveEvento(String xml) {
        // Implementação simplificada para extrair chNFe do XML
        int index = xml.indexOf("<chNFe>");
        if (index != -1) {
            return xml.substring(index + 7, index + 51);
        }
        return "desconhecido-" + System.currentTimeMillis();
    }

    private String determinarProximoSequencial(String chave) throws NfeException {
        List<XmlNotaFiscal> eventos = xmlNotaFiscalRepository.findByChaveAcessoStartingWith(chave + "-cce");
        int ultimoSequencial = eventos.stream()
                .map(xml -> {
                    String id = xml.getChaveAcesso();
                    return Integer.parseInt(id.substring(id.length() - 2));
                })
                .max(Integer::compare)
                .orElse(0);

        int proximoSequencial = ultimoSequencial + 1;
        if (proximoSequencial > 20) {
            throw new NfeException("Limite de 20 CC-e por NF-e atingido para a chave: " + chave);
        }
        return String.format("%02d", proximoSequencial);
    }

    public List<NotaFiscal> consultarNotasMesCorrente(String documentoEmitente) {
        YearMonth anoMesAtual = YearMonth.now();
        int ano = anoMesAtual.getYear();
        int mes = anoMesAtual.getMonthValue();

        return notaFiscalRepository.consultarNotasMesCorrente(ano, mes, documentoEmitente);
    }

    public List<NotaFiscal> consultarNotasPorPeriodo(String documentoEmitente, LocalDate dataInicial, LocalDate dataFinal) {
        return notaFiscalRepository.consultarNotasPorPeriodo(dataInicial, dataFinal, documentoEmitente);
    }

}