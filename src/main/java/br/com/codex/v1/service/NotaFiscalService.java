package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.fiscal.LoteNfe;
import br.com.codex.v1.domain.fiscal.NotaFiscal;
import br.com.codex.v1.domain.contabilidade.SerieNfe;
import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.*;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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

    @Autowired
    private LoteNfeRepository loteRepository;

    Integer ambienteNota;
    String estadoUf;

    /**
     * Executa o processo completo de emissão da NF-e.
     */
    @Transactional
    public NotaFiscalDto emitirNotaFiscal(NotaFiscalDto dto) throws Exception {
        logger.info("Iniciando emissão da NF-e para CNPJ: {}", dto.getDocumentoEmitente());
        ConfiguracoesNfe config = iniciarConfiguracao(dto);

        // 1. Obter o próximo número da série (com tratamento adequado do Optional)
        Optional<SerieNfe> serieOpt = serieNfeRepository.findByNumeroSerieAndCnpjAndAmbiente(dto.getSerie(), dto.getDocumentoEmitente(), dto.getTipoAmbiente());

        System.out.println("Serie: "+dto.getSerie());
        System.out.println("Documento: "+dto.getDocumentoEmitente());
        System.out.println("Ambiente: "+dto.getTipoAmbiente());

        if (serieOpt.isEmpty()) {
            throw new Exception("Série não encontrada para os parâmetros informados");
        }

        SerieNfe serie = serieOpt.get();
        String numeroNota = String.valueOf(serie.getUltimoNumero() + 1); // Incrementa o último número

        // 2. Atualiza o último número usado na série
        serie.setUltimoNumero(serie.getUltimoNumero() + 1);
        serieNfeRepository.save(serie);

        // 3. Gerar chave de acesso COMPLETA
        String chaveAcesso = gerarChaveAcessoCompleta(dto.getCodigoUf(), dto.getDocumentoEmitente(),
                dto.getModelo(), dto.getSerie(), numeroNota, dto.getTipo());

        dto.setChave(chaveAcesso);

        TEnviNFe enviNFe = montarNotaFiscal(dto);
        salvaLoteNotaFiscal(enviNFe, config);

        enviNFe = assinarNotaFiscal(enviNFe, config);
        validarNotaFiscal(enviNFe, config);
        TRetEnviNFe retorno = transmiteNotaFiscal(enviNFe, config);

        dto.setChave(retorno.getProtNFe().getInfProt().getChNFe());
        dto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
        dto.setNumeroProtocolo(retorno.getProtNFe().getInfProt().getNProt());
        dto.setMotivoProtocolo(retorno.getProtNFe().getInfProt().getXMotivo());

        String xml = XmlNfeUtil.objectToXml(enviNFe, config.getEncode());

        if (retorno.getProtNFe().getInfProt().getCStat().equals("100")) {
            salvarXmlNotaFiscal(dto.getChave() + "-proc", xml);
        }
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
            ambienteNota = ambienteNotaFiscal.get().getCodigoAmbiente();
            notaFiscalDto.setTipoAmbiente(String.valueOf(ambienteNota));
        } else {
            throw new NfeException("O ambiente da nota fiscal não está parametrizado");
        }

        if(cert.isPresent()){
            notaFiscalDto.setCodigoUf(cert.get().getUf());
            estadoUf = cert.get().getUf();
        }else{
            throw new NfeException("A sigla do estado não está parametrizado na tabela 'configuracao_certificado'");
        }

        if (cert.isEmpty()) {
            throw new NfeException("Certificado não encontrado para CNPJ: " + notaFiscalDto.getDocumentoEmitente());
        }

        try {
            String senhaDecodificada = Base64Util.decode(cert.get().getSenha());
            Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);

            int codigoAmbiente = ambienteNotaFiscal.get().getCodigoAmbiente();
            AmbienteEnum ambienteEnum = converterCodigoParaAmbienteEnum(codigoAmbiente);

            return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.valueOf(cert.get().getUf()),
                    ambienteEnum, certificado, "schemas"
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

        // Valida e atualiza o lote
        Optional<LoteNfe> loteOpt = loteRepository.findByIdLoteAndCnpjAndAmbiente(nota.getSerie(), nota.getDocumentoEmitente(), ambienteNota);
        LoteNfe lote;
        if (loteOpt.isEmpty()) {
            lote = new LoteNfe();
            lote.setCnpjEmitente(nota.getDocumentoEmitente());
            lote.setAmbiente(ambienteNota);
            lote.setTipoDocumento(DocumentoEnum.NFE.name());

            // Tratamento para número da nota
            int numeroNota;
            if (nota.getNumero() == null || nota.getNumero().isEmpty()) {
                numeroNota = 1;
            } else {
                try {
                    numeroNota = Integer.parseInt(nota.getNumero());
                } catch (NumberFormatException e) {
                    throw new NfeException("Número da NF-e inválido: " + nota.getNumero());
                }
            }
            lote.setUltimoNumero(String.valueOf(numeroNota));
            lote.setStatus("Ativo");
            lote.setDataEnvio(LocalDateTime.now());
        } else {
            lote = loteOpt.get();
            int numeroNotaAtual = Integer.parseInt(nota.getNumero());
            int ultimoNumero = Integer.parseInt(lote.getUltimoNumero());

            if (numeroNotaAtual <= ultimoNumero) {
                throw new NfeException("Número da NF-e já utilizado para o lote: " + nota.getSerie());
            }
            lote.setUltimoNumero(String.valueOf(numeroNotaAtual));
        }
        loteRepository.save(lote);

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
    public void validarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe config) throws NfeException, JAXBException, IOException {
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        Path diretorio = Paths.get("/tmp/xml_notas_" + dataFormatada);

        logger.info("Validando NF-e, ID Lote: {}", enviNFe.getIdLote());
        String xml = XmlNfeUtil.objectToXml(enviNFe, config.getEncode());

        /*if (!Files.exists(diretorio)) {
            Files.createDirectories(diretorio);
            System.out.println("Diretório criado: " + diretorio);
        }
        Path arquivo = diretorio.resolve("nota.xml");
        Files.write(arquivo, xml.getBytes());*/
        Files.write(Paths.get("nota.xml"), xml.getBytes()); // Salva para análise

        if (!new Validar().isValidXml(config, xml, ServicosEnum.CONSULTA_RECIBO)) {
            throw new NfeException("XML inválido segundo schemas da SEFAZ");
        }

        String xmlAssinado = Assinar.assinaNfe(config, xml, AssinaturaEnum.NFE);
        XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
    }

    /**
     * Transmite a NF-e para a SEFAZ.
     */
    public TRetEnviNFe transmiteNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException {
        logger.info("Enviando NF-e, ID Lote: {}", enviNFe.getIdLote());
        TRetConsStatServ status = consultarStatusServico(configuracoes);
        if (!status.getCStat().equals("107")) { // Serviço em operação
            throw new NfeException("SEFAZ offline - Ativar contingência");
        }

        TRetEnviNFe retorno = Nfe.enviarNfe(configuracoes, enviNFe, DocumentoEnum.NFE);
        if (!"100".equals(retorno.getProtNFe().getInfProt().getCStat())) {
            throw new NfeException("Erro ao enviar NF-e- " + retorno.getProtNFe().getInfProt().getXMotivo());
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
                // Agora usando o métudo correto com 4 parâmetros
                br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento retCanc =
                        Nfe.cancelarNfe(config, envCanc, false, DocumentoEnum.NFE);
                xmlRetorno = XmlNfeUtil.objectToXml(retCanc, config.getEncode());
            }
            else {
                // Para outros tipos de evento, usar o métudo genérico
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

    /**
     * Salva a nota fiscal no banco
     */
    @Transactional
    public TRetEnviNFe salvaLoteNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe config) throws NfeException {
        // 1. Salva o lote antes do envio
        LoteNfe lote = new LoteNfe();
        lote.setIdLote(enviNFe.getIdLote());
        lote.setCnpjEmitente(config.getCertificado().getCnpjCpf());
        lote.setAmbiente(Integer.valueOf(config.getAmbiente().getCodigo()));
        lote.setDataEnvio(LocalDateTime.now());
        lote.setStatus("ENVIANDO");
        lote.setQuantidadeNotas(enviNFe.getNFe().size());
        loteRepository.save(lote);

        try {
            // 2. Envia para SEFAZ
            TRetEnviNFe retorno = Nfe.enviarNfe(config, enviNFe, DocumentoEnum.NFE);

            // 3. Atualiza o lote com a resposta
            lote.setStatus("PROCESSADO");
            lote.setProtocolo(retorno.getProtNFe().getInfProt().getNProt());
            lote.setDataEnvio(LocalDateTime.now());
            lote.setXmlResposta(XmlNfeUtil.objectToXml(retorno, config.getEncode()));

            return retorno;
        } catch (Exception e) {
            // 4. Registra falha
            lote.setStatus("ERRO");
            lote.setXmlResposta(e.getMessage());
            throw new NfeException("Erro ao transmitir nota fiscal: " + e);
        }
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

    private AmbienteEnum converterCodigoParaAmbienteEnum(int codigo) throws NfeException {
        for (AmbienteEnum ambiente : AmbienteEnum.values()) {
            if (Integer.parseInt(ambiente.getCodigo()) == codigo) {
                return ambiente;
            }
        }
        throw new NfeException("Código de ambiente inválido: " + codigo);
    }

    private String gerarChaveAcessoCompleta(String uf, String cnpj, String modelo,String serie, String numero, String tipoNota) {
        // Validações
        Objects.requireNonNull(numero, "Número da nota é obrigatório");

        EstadosEnum estado = EstadosEnum.valueOf(uf);
        int codigoUf = Integer.parseInt(estado.getCodigoUF());

        // Formatação dos componentes
        String chaveParcial = String.format("%02d", codigoUf) +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM")) +
                String.format("%014d", new BigInteger(cnpj.replaceAll("\\D", ""))) +
                String.format("%02d", Integer.parseInt(modelo)) +
                String.format("%03d", Integer.parseInt(serie)) +
                String.format("%09d", Integer.parseInt(numero)) +
                tipoNota + // Tipo de emissão (1=Normal)
                String.format("%08d", ThreadLocalRandom.current().nextInt(100000000));

        return chaveParcial + calcularDV(chaveParcial);
    }

    private String calcularDV(String chave43posicoes) {
        int[] pesos = {2,3,4,5,6,7,8,9,2,3,4,5,6,7,8,9,2,3,4,5,6,7,8,9,2,3,4,5,6,7,8,9,2,3,4,5,6,7,8,9,2,3,4};
        int soma = IntStream.range(0, 43)
                .map(i -> Character.getNumericValue(chave43posicoes.charAt(i)) * pesos[i])
                .sum();
        int resto = soma % 11;
        return (resto < 2) ? "0" : String.valueOf(11 - resto);
    }
}