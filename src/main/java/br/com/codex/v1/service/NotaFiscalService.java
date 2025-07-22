package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.codex.v1.domain.contabilidade.SerieNfe;
import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.fiscal.LoteNfe;
import br.com.codex.v1.domain.fiscal.NotaFiscal;
import br.com.codex.v1.domain.repository.*;
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
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.*;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISAliq;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det.Prod;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Total.ICMSTot;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TInutNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import br.com.codex.v1.utilitario.RemoveExcessoCfop;

import static br.com.codex.v1.utilitario.FormatadorDecimal.*;

@Service
public class NotaFiscalService {
    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalService.class);

    // Constantes
    private static final String VERSAO_APLICATIVO = "4.00";
    private static final String CODIGO_PAIS_BRASIL = "1058";
    private static final String NOME_PAIS_BRASIL = "Brasil";
    private static final String DANFE_NORMAL = "1";
    private static final String EMISSAO_PROPRIA = "0";

    @Autowired
    private ConfiguracaoCertificadoRepository certificadoRepository;

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Autowired
    private SerieNfeRepository serieNfeRepository;

    @Autowired
    private ControleNsuService controleNsuService;

    @Autowired
    private XmlNotaFiscalRepository xmlNotaFiscalRepository;

    @Autowired
    private LoteNfeRepository loteRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    private Integer ambienteNota, codigoCnf;
    private String estadoUf;

    /**
     * Inicia as configurações necessárias
     */
    public ConfiguracoesNfe iniciarConfiguracoes(NotaFiscalDto dto) throws NfeException {
        logger.info("Iniciando configurações para NF-e, CNPJ: {}", dto.getDocumentoEmitente());

        Optional<ConfiguracaoCertificado> cert = certificadoRepository.findByCnpj(dto.getDocumentoEmitente());
        Optional<AmbienteNotaFiscal> ambienteNotaFiscal = ambienteNotaFiscalRepository.findById(1L);

        if (ambienteNotaFiscal.isEmpty()) {
            throw new NfeException("O ambiente da nota fiscal não está parametrizado");
        }

        if (cert.isEmpty()) {
            throw new NfeException("Erro ao obter configurações do certificado digital");
        }

        try {
            ambienteNota = ambienteNotaFiscal.get().getCodigoAmbiente();
            dto.setTipoAmbiente(String.valueOf(ambienteNota));

            estadoUf = String.valueOf(EstadosEnum.valueOf(cert.get().getUf()).getCodigoUF());//aqui obter o código do stado e não a sigla
            dto.setCodigoUf(cert.get().getUf());

            String senhaDecodificada = Base64Util.decode(cert.get().getSenha());
            Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);

            AmbienteEnum ambienteEnum = converterCodigoParaAmbienteEnum(ambienteNota);

            return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.valueOf(cert.get().getUf()), ambienteEnum,
                    certificado, "schemas");
        } catch (Exception e) {
            logger.error("Erro ao configurar certificado", e);
            throw new NfeException("Falha na configuração do certificado", e);
        }
    }

    /**
     * Emite uma NF-e seguindo o modelo da Wiki
     */
     @Transactional
    public NotaFiscalDto emitirNotaFiscal(NotaFiscalDto dto) throws Exception {
        try {
            // 1. Configurações iniciais
            ConfiguracoesNfe config = iniciarConfiguracoes(dto);

            // 2. Obtém número da nota
            String numeroNota = obterProximoNumeroNota(dto);
            dto.setNumero(numeroNota);

            // 2.1. Gera código numérico (cNF) de 8 dígitos sem zero à esquerda
            String codigoNumerico = String.valueOf(10000000 + new Random().nextInt(89999999));
            codigoCnf = Integer.parseInt(codigoNumerico);

            // 3. Monta a chave de acesso
            String chave = montarChaveAcesso(config, dto, numeroNota);
            dto.setChave(chave);

            // 4. Monta a estrutura da NFe
            TEnviNFe enviNFe = montarEstruturaNFe(config, dto, chave);

            // 5. Processa o envio da NFe
            return processarEnvioNFe(config, enviNFe, dto);

        } catch (Exception e) {
            logger.error("Erro ao emitir NF-e", e);
            throw e;
        }
    }

    /**
     * Obtém o próximo número da nota fiscal
     */
    private String obterProximoNumeroNota(NotaFiscalDto dto) throws Exception {
        Optional<SerieNfe> serieOpt = serieNfeRepository.findByNumeroSerieAndCnpjAndAmbiente(
                dto.getSerie(), dto.getDocumentoEmitente(), dto.getTipoAmbiente());

        if (serieOpt.isEmpty()) {
            throw new Exception("Série não encontrada para os parâmetros informados");
        }

        SerieNfe serie = serieOpt.get();
        String numeroNota = String.valueOf(serie.getUltimoNumero() + 1);

        // Atualiza o último número usado na série
        serie.setUltimoNumero(serie.getUltimoNumero() + 1);
        serieNfeRepository.save(serie);

        return numeroNota;
    }

    /**
     * Monta a chave de acesso da NF-e
     */
    private String montarChaveAcesso(ConfiguracoesNfe config, NotaFiscalDto dto, String numeroNota) {
        String cnf = ChaveUtil.completarComZerosAEsquerda(numeroNota, 8);
        ChaveUtil chaveUtil = new ChaveUtil(config.getEstado(), dto.getDocumentoEmitente(),
                dto.getModelo(), Integer.parseInt(dto.getSerie()),
                Integer.parseInt(numeroNota), dto.getTipo(), cnf, LocalDateTime.now());
        return chaveUtil.getChaveNF();
    }

    /**
     * Monta a estrutura completa da NF-e
     */
    private TEnviNFe montarEstruturaNFe(ConfiguracoesNfe config, NotaFiscalDto dto, String chave) throws NfeException {
        InfNFe infNFe = new InfNFe();
        infNFe.setId(chave);
        infNFe.setVersao(ConstantesUtil.VERSAO.NFE);

        // Preenche as partes da NFe
        infNFe.setIde(preencherIde(config, dto, chave));
        infNFe.setEmit(preencherEmitente(config, dto));
        infNFe.setDest(preencherDestinatario(dto));
        infNFe.getDet().addAll(preencherDetalhes(dto));
        infNFe.setTotal(preencherTotais(dto));
        infNFe.setTransp(preencherTransporte(dto));
        infNFe.setPag(preencherPagamento(dto));
        infNFe.setInfAdic(preencherInformacoesAdicionais(dto));

        // Cria a TNFe
        TNFe nfe = new TNFe();
        nfe.setInfNFe(infNFe);

        // Cria o EnviNFe
        TEnviNFe enviNFe = new TEnviNFe();
        enviNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        enviNFe.setIdLote(gerarIdLote());
        enviNFe.setIndSinc("1"); // Processamento síncrono
        enviNFe.getNFe().add(nfe);

        return enviNFe;
    }

    /**
     * Preenche os dados do IDE
     */
    private Ide preencherIde(ConfiguracoesNfe config, NotaFiscalDto dto, String chave) throws NfeException {
        Ide ide = new Ide();
        ide.setCUF(EstadosEnum.valueOf(dto.getCodigoUf()).getCodigoUF());
        ide.setCNF(String.valueOf(codigoCnf));
        ide.setNatOp(RemoveExcessoCfop.limitarDescricao(dto.getNaturezaOperacao()));
        ide.setMod(dto.getModelo());
        ide.setSerie(dto.getSerie());
        ide.setNNF(dto.getNumero());
        ide.setDhEmi(XmlNfeUtil.dataNfe(LocalDateTime.now()));
        ide.setTpNF(dto.getTipo());
        ide.setIdDest(dto.getLocalDestino());
        ide.setCMunFG(dto.getCodigoMunicipioEmitente());
        ide.setTpImp(DANFE_NORMAL);
        ide.setTpEmis(dto.getTipo());
        ide.setCDV(chave.substring(chave.length() - 1));
        ide.setTpAmb(dto.getTipoAmbiente());
        ide.setFinNFe(String.valueOf(dto.getFinalidadeEmissao()));
        ide.setIndFinal(String.valueOf(dto.getConsumidorFinal()));
        ide.setIndPres(dto.getIndicadorPresenca());
        ide.setIndIntermed(dto.getIndicadorIntermediario());
        ide.setProcEmi(EMISSAO_PROPRIA);
        ide.setVerProc(VERSAO_APLICATIVO);
        if ("65".equals(dto.getModelo())) {
            ide.setIndPres("1"); // Operação presencial para NFC-e
            ide.setIndFinal("1"); // Consumidor final
        }

        return ide;
    }

    /**
     * Preenche os dados do Emitente
     */
    private Emit preencherEmitente(ConfiguracoesNfe config, NotaFiscalDto dto) {
        Emit emit = new Emit();
        emit.setCNPJ(dto.getDocumentoEmitente());
        emit.setXNome(dto.getRazaoSocialEmitente());
        emit.setXFant(dto.getNomeFantasiaEmitente());
        emit.setIE(dto.getInscricaoEstadualEmitente());
        emit.setIEST(dto.getInscricaoEstadualStEmitente() != null ? dto.getInscricaoEstadualStEmitente().toString() : null);
        emit.setIM(dto.getInscricaoMunicipalEmitente() != null ? dto.getInscricaoMunicipalEmitente().toString() : null);
        emit.setCRT(dto.getRegimeTributarioEmitente().toString());

        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(dto.getLogradouroEmitente());
        enderEmit.setNro(dto.getNumeroEnderecoEmitente());
        enderEmit.setXBairro(dto.getBairroEmitente());
        enderEmit.setCMun(dto.getCodigoMunicipioEmitente());
        enderEmit.setXMun(dto.getNomeMunicipioEmitente());
        enderEmit.setUF(TUfEmi.fromValue(dto.getUfEmitente()));
        enderEmit.setCPais(CODIGO_PAIS_BRASIL);
        enderEmit.setXPais(NOME_PAIS_BRASIL);
        enderEmit.setCEP(dto.getCepEmitente());
        enderEmit.setFone(dto.getTelefoneEmitente());
        emit.setEnderEmit(enderEmit);

        return emit;
    }

    /**
     * Preenche os dados do Destinatário
     */
    private Dest preencherDestinatario(NotaFiscalDto dto) {
        Dest dest = new Dest();
        dest.setCNPJ(dto.getDocumentoDestinatario());
        dest.setXNome(dto.getRazaoSocialDestinatario());
        dest.setIE(dto.getInscricaoEstadualDestinatario());
        dest.setIndIEDest(dto.getIndicadorInscricaoEstadualDestinatario().toString());
        dest.setEmail(dto.getEmailDestinatario());

        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr(dto.getLogradouroDestinatario());
        enderDest.setNro(dto.getNumeroEnderecoDestinatario());
        enderDest.setXBairro(dto.getBairroDestinatario());
        enderDest.setCMun(dto.getCodigoMunicipioDestinatario());
        enderDest.setXMun(dto.getNomeMunicipioDestinatario());
        enderDest.setUF(TUf.fromValue(dto.getUfDestinatario()));
        estadoUf = String.valueOf(TUf.fromValue(dto.getUfDestinatario()));
        enderDest.setCEP(dto.getCepDestinatario());
        enderDest.setCPais(CODIGO_PAIS_BRASIL);
        enderDest.setXPais(NOME_PAIS_BRASIL);
        enderDest.setFone(dto.getTelefoneDestinatario());
        dest.setEnderDest(enderDest);

        return dest;
    }

    /**
     * Preenche os detalhes dos produtos
     */
    private List<Det> preencherDetalhes(NotaFiscalDto dto) {
        List<Det> detalhes = new ArrayList<>();
        List<NotaFiscalItemDto> itens = dto.getItens();

        for (int i = 0; i < itens.size(); i++) {
            NotaFiscalItemDto item = itens.get(i);
            Det det = new Det();
            det.setNItem(String.valueOf(i + 1));

            // Preenche dados do produto
            Prod prod = new Prod();
            prod.setCProd(item.getCodigoProduto());
            prod.setCEAN(item.getCean() != null && item.getCean().matches("^[0-9]{13,14}$") ? item.getCean() : "SEM GTIN");
            prod.setXProd(item.getNomeProduto());
            prod.setNCM(item.getNcmSh().replace(".", ""));
            prod.setCEST(item.getCest().replace(".", ""));
            prod.setCFOP(item.getCfop());
            prod.setQCom(item.getQuantidadeComercial().toString());
            prod.setUCom(item.getUnidadeComercial());
            prod.setVUnCom(formatar(item.getValorUnitarioComercial()));
            prod.setVProd(formatar(item.getValorTotalProdutos()));
            prod.setCEANTrib(prod.getCEAN());
            prod.setUTrib(item.getUnidadeTributacao());
            prod.setQTrib(item.getQuantidadeTributacao() != null ? item.getQuantidadeTributacao().toString() : "0");
            prod.setVUnTrib(formatar(item.getValorUnitarioTributacao()));
            prod.setVFrete(formatar(dto.getValorFrete().divide(new BigDecimal(dto.getItens().size()),2, RoundingMode.HALF_UP)));
            //prod.setVSeg((formatar(dto.getValorSeguro().divide(new BigDecimal(dto.getItens().size()), 2, RoundingMode.HALF_UP))));
            //prod.setVDesc((formatar(dto.getValorDesconto().divide(new BigDecimal(dto.getItens().size()), 2, RoundingMode.HALF_UP))));
            //prod.setVOutro((formatar(dto.getValorOutros().divide(new BigDecimal(dto.getItens().size()),2, RoundingMode.HALF_UP))));
            prod.setIndTot("1");

            det.setProd(prod);

            // Preenche impostos
            det.setImposto(preencherImpostos(item));

            detalhes.add(det);
        }
        return detalhes;
    }

    /**
     * Preenche os impostos
     */
    private Imposto preencherImpostos(NotaFiscalItemDto item) {
        Imposto imposto = new Imposto();
        ObjectFactory factory = new ObjectFactory();

        ICMS icms = new ICMS();
        String cst = item.getCstIcms();

        // Cálculo do FCP (Fundo de Combate à Pobreza)
        BigDecimal valorFcp = BigDecimal.ZERO;
        if (item.getAliqFcp() != null && item.getAliqFcp().compareTo(BigDecimal.ZERO) > 0) {
            // Usa o valor total do produto (vProd) como base, ignorando item.getBcFcp()
            BigDecimal baseFcp = item.getValorTotalProdutos().setScale(2, RoundingMode.HALF_UP);
            valorFcp = baseFcp.multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // Cálculo do FCP ST
        BigDecimal valorFcpSt = BigDecimal.ZERO;
        if (item.getAliqFcpSt() != null && item.getBcFcpSt() != null) {
            valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        }

        switch (cst) {
            case "00" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
                obj.setOrig(item.getOrigIcms());
                obj.setCST(cst);
                obj.setModBC(item.getModBc());

                // Cálculo da BC do ICMS
                BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                obj.setVBC(formatar(bcIcms));

                // Cálculo do valor do ICMS
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getAliqIcms() != null) {
                    valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setVICMS(formatar(valorIcms.toString()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setVFCP(formatar(valorFcp.toString()));
                icms.setICMS00(obj);
            }

            case "02" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS02 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS02();
                obj.setOrig(item.getOrigIcms());
                obj.setCST(cst);
                obj.setAdRemICMS(formatar(item.getAliqIcms()));
                obj.setQBCMono(formatar(item.getBcIcms()));
                obj.setVICMSMono(formatar(item.getValorIcms()));
                icms.setICMS02(obj);
            }

            case "10" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);
                obj.setModBC(item.getModBc());

                // Cálculos para ICMS normal
                BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getAliqIcms() != null) {
                    valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                // Cálculos para ICMS ST
                BigDecimal bcIcmsSt = (item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getAliqIcmsSt() != null) {
                    valorIcmsSt = bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                obj.setVBC(bcIcms.toString());
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt().toString()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVICMS(formatar(valorIcms.toString()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCP(formatar(valorFcp.toString()));
                obj.setVBCFCP(formatar(item.getBcFcp()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setVBCST(formatar(bcIcmsSt.toString()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVICMSSTDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setModBCST(item.getModBc());
                obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                icms.setICMS10(obj);
            }

            case "15" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS15 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS15();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);
                obj.setAdRemICMS(formatar(item.getAliqIcms()));
                obj.setQBCMono(formatar(item.getBcIcms()));
                obj.setVICMSMono(formatar(item.getValorIcms()));
                obj.setAdRemICMSReten(null);
                obj.setQBCMonoReten(null);
                obj.setVICMSMonoReten(null);
                obj.setPRedAdRem(null);
                obj.setQBCMonoReten(null);
                icms.setICMS15(obj);
            }

            case "20" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);
                obj.setModBC(item.getModBc());

                // Cálculo com redução da BC
                BigDecimal bcIcms = item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO;
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getAliqIcms() != null) {
                    valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                obj.setVBC(bcIcms.toString());
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setVFCP(formatar(valorFcp.toString()));
                obj.setVBCFCP(formatar(item.getBcFcp()));
                obj.setVICMS(formatar(valorIcms.toString()));
                obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                icms.setICMS20(obj);
            }

            case "30" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCST(cst);

                // Cálculo do FCP ST (Fundo de Combate à Pobreza por ST)
                if (item.getAliqFcpSt() != null && item.getBcFcpSt() != null) {
                    valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // Cálculo do ICMS ST
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getAliqIcmsSt() != null && item.getBcIcmsSt() != null) {
                    valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setModBCST(null);
                obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setVFCPST(formatar(valorFcpSt));
                obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()).compareTo(String.valueOf(BigDecimal.ZERO)) > 0 ? "1" : "0");
                obj.setVICMSST(formatar(valorIcmsSt));
                icms.setICMS30(obj);
            }

            case "51" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);

                // Modalidade de determinação da BC (0-Margem Valor Agregado, 1-Pauta, 2-Valor Operação)
                obj.setModBC(item.getModBc());

                // 2. CÁLCULO DO FCP (independente do diferimento)
                if(item.getBcFcp() != null && item.getAliqFcp() != null) {
                    valorFcp = item.getBcFcp().multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULOS DE ICMS (quando houver BC e alíquota)
                BigDecimal valorIcmsOperacao = BigDecimal.ZERO;
                BigDecimal valorIcmsDiferido = BigDecimal.ZERO;
                BigDecimal valorIcms = BigDecimal.ZERO;

                // Cálculo do ICMS (considerando que os valores já vêm calculados do DTO)
                valorIcms = (item.getValorIcms() != null ? item.getValorIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                valorIcmsDiferido = (item.getValorIcmsDiferido() != null ? item.getValorIcmsDiferido() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                valorIcmsOperacao = (item.getValorIcmsOperacao() != null ? item.getValorIcmsOperacao() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

                if(item.getBcIcms() != null && item.getAliqIcms() != null) {
                    // 3.1. Valor total do ICMS (antes do diferimento)
                    valorIcmsOperacao = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    // 3.2. Calcula diferimento com base no valor parcial (se existir)
                    if(item.getValorIcmsDiferido() != null) {
                        // Se o DTO já trouxer o valor diferido, usa esse valor
                        valorIcmsDiferido = item.getValorIcmsDiferido().setScale(2, RoundingMode.HALF_UP);
                        valorIcms = (valorIcmsOperacao.subtract(valorIcmsDiferido)).setScale(2, RoundingMode.HALF_UP);
                    } else {
                        // Caso contrário, considera ICMS integral (sem diferimento)
                        valorIcms = valorIcmsOperacao.setScale(2, RoundingMode.HALF_UP);
                    }
                }

                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setCBenefRBC(null);
                obj.setPDif(null);
                obj.setPFCPDif(null);
                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setVBC(formatar(item.getBcIcms()));
                obj.setVFCP(valorFcp.toString());
                obj.setVBCFCP(formatar(item.getBcFcp()));
                obj.setVFCPDif(null);
                obj.setVFCPEfet(null);
                obj.setVICMS(valorIcms.toString());
                obj.setVICMSDif(valorIcmsDiferido.toString());
                obj.setVICMSOp(valorIcmsOperacao.toString());
                icms.setICMS51(obj);
            }

            case "53" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS53 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS53();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);

                // Alíquota ad rem (por unidade de medida)
                BigDecimal aliqAdRem = item.getAliqIcms() != null ? item.getAliqIcms() : BigDecimal.ZERO;
                obj.setAdRemICMS(aliqAdRem.toString());

                // Quantidade tributada
                BigDecimal qtdTributada = item.getQuantidadeTributacao() != null ? item.getQuantidadeTributacao() : BigDecimal.ZERO;
                obj.setQBCMono(qtdTributada.toString());

                // Cálculo do valor do ICMS monofásico
                BigDecimal valorIcmsMono = qtdTributada.multiply(aliqAdRem).setScale(2, RoundingMode.HALF_UP);

                obj.setAdRemICMS(formatar(item.getAliqIcms()));
                obj.setAdRemICMSDif(null);
                obj.setQBCMono(formatar(item.getBcIcms()));
                obj.setQBCMonoDif(null);
                obj.setPDif(null);
                obj.setVICMSMono(formatar(valorIcmsMono.toString()));
                obj.setVICMSMonoDif(null);
                obj.setVICMSMonoOp(null);
                icms.setICMS53(obj);
            }

            case "60" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);

                // Cálculo do ICMS ST retido
                BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                if(item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsStRetido = item.getBcIcmsStRetido().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // Cálculo do FCP ST retido
                BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                if(item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                    valorFcpStRetido = item.getBcFcpStRetido().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()));
                obj.setPST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()));
                obj.setVICMSSTRet(formatar(valorIcmsStRetido));
                obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()));
                obj.setPFCPSTRet(formatar(item.getAliqFcpSt()));
                obj.setVFCPSTRet(formatar(valorFcpStRetido));
                obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()));
                obj.setVBCEfet(null);
                obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()));
                obj.setVICMSEfet(formatar(item.getValorIcmsEfetivo()));
                icms.setICMS60(obj);
            }

            case "61" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS61 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS61();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);

                // 2. Cálculo alternativo (caso não tenha alíquota específica de retenção)
                if (item.getValorIcms() != null) {
                    // Se existir valor de ICMS no item, usa como valor retido (ajuste conforme necessidade)
                    obj.setVICMSMonoRet(formatar(item.getValorIcms()));

                    // Campos de quantidade e alíquota retidos (opcionais) - deixamos null
                    obj.setQBCMonoRet(null);
                    obj.setAdRemICMSRet(null);
                } else {
                    // Se não houver valor de ICMS, mantém tudo null (conforme NT 2023.001)
                    obj.setVICMSMonoRet(null);
                    obj.setQBCMonoRet(null);
                    obj.setAdRemICMSRet(null);
                }
                icms.setICMS61(obj);
            }

            case "70" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);
                obj.setModBC(item.getModBc());

                // Cálculo com redução da BC
                BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getAliqIcms() != null) {
                    valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                // Cálculo do ICMS ST
                BigDecimal bcIcmsSt = item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO;
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getAliqIcmsSt() != null) {
                    valorIcmsSt = bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setVBC(formatar(bcIcms));
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setVICMS(formatar(valorIcms));
                obj.setVBCFCP(formatar(item.getBcFcp()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setVFCP(valorFcp.toString());
                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setVBCST(formatar(bcIcmsSt));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(item.getValorIcmsSt()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt));
                obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado())); //Indica se o valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd).0: Valor do ICMS desonerado (vICMSDeson) não deduz do valor do item (vProd) / total da NF-e 1: Valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd) / total da NF-e.
                obj.setVICMSSTDeson(null);
                obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                icms.setICMS70(obj);
            }

            case "90" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);

                // 2. CÁLCULO DO ICMS PRÓPRIO (quando aplicável)
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getBcIcms() != null && item.getAliqIcms() != null) {
                    valorIcms = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO FCP (Fundo de Combate à Pobreza)
                if (item.getBcFcp() != null && item.getAliqFcp() != null) {
                    valorFcp = item.getBcFcp().multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 4. CÁLCULO DO ICMS ST (quando aplicável)
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 5. CÁLCULO DO FCP ST
                if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                    valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setModBC(item.getModBc());
                obj.setVBC(formatar(item.getBcIcms()));
                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setVICMS(formatar(valorIcms.toString()));
                obj.setVBCFCP(formatar(item.getBcFcp()));
                obj.setPFCP(formatar(item.getAliqFcp()));
                obj.setVFCP(formatar(valorFcp.toString()));
                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                obj.setVICMSSTDeson(null);
                obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                icms.setICMS90(obj);
            }

            case "part" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCST(cst);
                obj.setModBC(item.getModBc());

                // 2. CÁLCULO DO ICMS PRÓPRIO
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getBcIcms() != null && item.getAliqIcms() != null) {
                    valorIcms = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO ICMS ST (quando aplicável)
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 4. CÁLCULO DO FCP ST
                if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                    valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setVBC(formatar(item.getBcIcms()));
                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setVICMS(valorIcms.toString());
                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                obj.setPBCOp(formatar(item.getPercentBcOperacao()));
                obj.setUFST(TUf.valueOf(estadoUf)); // UF de destino da mercadoria
                icms.setICMSPart(obj);
            }

            case "st" ->{
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSST();
                obj.setOrig(item.getOrigIcms()!= null ? item.getOrigIcms() : "0");
                obj.setCST(cst);

                // 2. CÁLCULO DO ICMS ST RETIDO
                BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                if (item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsStRetido = item.getBcIcmsStRetido().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO FCP ST RETIDO
                BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                if (item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                    valorFcpStRetido = item.getBcFcpStRetido().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 4. CÁLCULO EFETIVO (quando aplicável)
                BigDecimal valorIcmsEfetivo = BigDecimal.ZERO;
                if (item.getBcIcmsEfetivo() != null && item.getAliqIcmsEfetivo() != null) {
                    valorIcmsEfetivo = item.getBcIcmsEfetivo().multiply(item.getAliqIcmsEfetivo())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()));
                obj.setPST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()));
                obj.setVICMSSTRet(formatar(valorIcmsStRetido));
                obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()));
                obj.setPFCPSTRet(formatar(item.getAliqFcpSt()));
                obj.setVFCPSTRet(formatar(valorFcpStRetido));
                obj.setVBCSTDest(formatar(item.getBcIcmsStDestino()));
                obj.setVICMSSTDest(formatar(item.getValorIcmsStDestino()));
                obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()));
                obj.setVBCEfet(formatar(item.getBcIcmsEfetivo()));
                obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()));
                obj.setVICMSEfet(formatar(valorIcmsEfetivo.compareTo(BigDecimal.ZERO) > 0 ? valorIcmsEfetivo : BigDecimal.ZERO));
                icms.setICMSST(obj);
            }

            case "101" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCSOSN(cst);

                item.setBcIcms(BigDecimal.ZERO);
                item.setValorIcms(BigDecimal.ZERO);
                item.setBcFcp(new BigDecimal("0.00"));
                item.setValorFcp(new BigDecimal("0.00"));

                if (item.getValorCredIcmsSn() != null) {
                    // Se existir valor de crédito diretamente no item
                    obj.setVCredICMSSN(formatar(item.getValorCredIcmsSn()));

                    // Se tiver alíquota de crédito (opcional)
                    obj.setPCredSN(formatar(item.getAliqCredSn()));
                } else {
                    obj.setPCredSN("0.00");
                    obj.setVCredICMSSN("0.00");
                }
                icms.setICMSSN101(obj);
            }

            case "102" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCSOSN(cst);

                item.setBcFcp(new BigDecimal("0.00"));
                item.setValorFcp(new BigDecimal("0.00"));

                icms.setICMSSN102(obj);
            }

            case "201" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCSOSN(cst);

                // 2. CÁLCULO DO ICMS ST
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO FCP ST
                if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                    valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 4. CÁLCULO DO CRÉDITO PRESUMIDO (quando houver)
                BigDecimal valorCredito = BigDecimal.ZERO;
                if (item.getAliqCredSn() != null && item.getValorCredIcmsSn() != null) {
                    valorCredito = item.getValorCredIcmsSn(); // Usa valor direto se existir
                }

                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                obj.setPCredSN(formatar(item.getAliqCredSn()));
                obj.setVCredICMSSN(formatar(valorCredito.compareTo(BigDecimal.ZERO) > 0 ? valorCredito.toString() : null));
                icms.setICMSSN201(obj);
            }

            case "202" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCSOSN(cst);

                // 2. CÁLCULO DO ICMS ST
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO FCP ST
                if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                    valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(item.getBcIcmsSt()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                icms.setICMSSN202(obj);
            }

            case "500" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                obj.setCSOSN(cst);

                // 2. CÁLCULO DO ICMS ST RETIDO
                BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                if (item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                    valorIcmsStRetido = item.getBcIcmsStRetido()
                            .multiply(item.getAliqIcmsSt())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 3. CÁLCULO DO FCP ST RETIDO
                BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                if (item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                    valorFcpStRetido = item.getBcFcpStRetido()
                            .multiply(item.getAliqFcpSt())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                // 4. CÁLCULO EFETIVO (quando aplicável)
                BigDecimal valorIcmsEfetivo = BigDecimal.ZERO;
                if (item.getBcIcmsEfetivo() != null && item.getAliqIcmsEfetivo() != null) {
                    valorIcmsEfetivo = item.getBcIcmsEfetivo()
                            .multiply(item.getAliqIcmsEfetivo())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()));
                obj.setPST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()));
                obj.setVICMSSTRet(formatar(valorIcmsStRetido.toString()));
                obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()));
                obj.setPFCPSTRet(formatar(valorFcpStRetido.toString()));
                obj.setVFCPSTRet(formatar(item.getValorFcpStRetido()));
                obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()));
                obj.setVBCEfet(formatar(item.getBcIcmsEfetivo()));
                obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()));
                obj.setVICMSEfet(formatar(valorIcmsEfetivo.compareTo(BigDecimal.ZERO) > 0 ? valorIcmsEfetivo.toString() : null));
                icms.setICMSSN500(obj);
            }

            case "900" -> {
                TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
                obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                obj.setCSOSN(cst);
                obj.setModBC(item.getModBc());

                // Cálculos para ICMS normal
                BigDecimal bcIcms = item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO;
                BigDecimal valorIcms = BigDecimal.ZERO;
                if (item.getAliqIcms() != null) {
                    valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                }

                // Cálculos para ICMS ST
                BigDecimal bcIcmsSt = item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO;
                BigDecimal valorIcmsSt = BigDecimal.ZERO;
                if (item.getAliqIcmsSt() != null) {
                    valorIcmsSt = bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                }

                obj.setVBC(formatar(bcIcms.toString()));
                obj.setPRedBC(formatar(item.getPercentRedBc()));
                obj.setPICMS(formatar(item.getAliqIcms()));
                obj.setVICMS(formatar(valorIcms.toString()));
                obj.setModBCST(formatar(item.getModBcSt()));
                obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                obj.setVBCST(formatar(bcIcmsSt.toString()));
                obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                obj.setVICMSST(formatar(valorIcmsSt.toString()));
                obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                obj.setPFCPST(formatar(item.getAliqFcpSt()));
                obj.setVFCPST(formatar(valorFcpSt.toString()));
                obj.setPCredSN(formatar(item.getAliqCredSn()));
                obj.setVCredICMSSN(formatar(item.getValorCredIcmsSn()));
                icms.setICMSSN900(obj);
            }
        }

        imposto.getContent().add(factory.createTNFeInfNFeDetImpostoICMS(icms));

        // === PIS ===
        PIS pis = new PIS();
        PISAliq pisAliq = new PISAliq();
        pisAliq.setCST(item.getCstPis());

        // Cálculo do PIS
        BigDecimal valorPis = BigDecimal.ZERO;
        if (item.getBcPis() != null && item.getAliqPis() != null) {
            valorPis = item.getBcPis().multiply(item.getAliqPis()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        }

        pisAliq.setVBC(formatar(item.getBcPis()));
        pisAliq.setPPIS(formatar(item.getAliqPis()));
        pisAliq.setVPIS(formatar(valorPis));
        pis.setPISAliq(pisAliq);
        imposto.getContent().add(factory.createTNFeInfNFeDetImpostoPIS(pis));

        // === Cofins ===
        COFINS cofins = new COFINS();
        COFINSAliq cofinsAliq = new COFINSAliq();
        cofinsAliq.setCST(item.getCstCofins());

        // Cálculo do COFINS
        BigDecimal valorCofins = BigDecimal.ZERO;
        if (item.getBcCofins() != null && item.getAliqCofins() != null) {
            valorCofins = item.getBcCofins().multiply(item.getAliqCofins()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        cofinsAliq.setVBC(formatar(item.getBcCofins()));
        cofinsAliq.setPCOFINS(formatar(item.getAliqCofins()));
        cofinsAliq.setVCOFINS(formatar(valorCofins));
        cofins.setCOFINSAliq(cofinsAliq);
        imposto.getContent().add(factory.createTNFeInfNFeDetImpostoCOFINS(cofins));

        return imposto;
    }

    /**
     * Preenche os totais da NF-e
     */
    private Total preencherTotais(NotaFiscalDto dto) {

        Total total = new Total();
        ICMSTot icmsTot = new ICMSTot();

        BigDecimal totalBaseCalculoICMS = BigDecimal.ZERO;

        for (NotaFiscalItemDto item : dto.getItens()) {
            if (item.getBcIcms() != null) {
                totalBaseCalculoICMS = totalBaseCalculoICMS.add(item.getBcIcms());
            }
        }

        icmsTot.setVBC(formatar(totalBaseCalculoICMS));
        icmsTot.setVICMS(formatar(dto.getValorIcms()));
        icmsTot.setVICMSDeson(formatar(dto.getValorIcmsDesonerado()));
        icmsTot.setVFCP(formatar(dto.getValorFcp()));
        icmsTot.setVBCST(formatar(dto.getValorBaseCalculoSt()));
        icmsTot.setVST(formatar(dto.getValorSt()));
        icmsTot.setVFCPST(formatar(dto.getValorFcpSt()));
        icmsTot.setVFCPSTRet(formatar(dto.getValorFcpStRetido()));
        icmsTot.setVProd(formatar(dto.getValorProdutos()));
        icmsTot.setVFrete(formatar(dto.getValorFrete()));
        icmsTot.setVSeg(formatar(dto.getValorSeguro()));
        icmsTot.setVDesc(formatar(dto.getValorDesconto()));
        icmsTot.setVII(formatar(dto.getValorIi()));
        icmsTot.setVIPI(formatar(dto.getValorIpi()));
        icmsTot.setVIPIDevol(formatar(dto.getValorIpiDevolucao()));
        icmsTot.setVPIS(formatar(dto.getValorPis()));
        icmsTot.setVCOFINS(formatar(dto.getValorCofins()));
        icmsTot.setVOutro(formatar(dto.getValorOutros()));
        icmsTot.setVNF(formatar(dto.getValorTotal()));

        total.setICMSTot(icmsTot);
        return total;
    }

    /**
     * Preenche os dados de transporte
     */
    private Transp preencherTransporte(NotaFiscalDto dto) {
        Transp transp = new Transp();
        transp.setModFrete(dto.getModalidadeFrete());

        // Transportadora
        Transp.Transporta transporta = new Transp.Transporta();
        transporta.setCNPJ(dto.getCnpjTransportador());
        transporta.setXNome(dto.getNomeTransportador());
        transporta.setXEnder(dto.getEnderecoTransportador());
        transporta.setXMun(dto.getMunicipioTransportador());

        transp.setTransporta(transporta);

        // Volume
        Transp.Vol volume = new Transp.Vol();
        volume.setQVol(String.valueOf(dto.getQuantidadeVolumes()));
        volume.setPesoL(formatarPeso(Double.valueOf(dto.getPesoLiquido())));
        volume.setPesoB(formatarPeso(Double.valueOf(dto.getPesoBruto())));

        transp.getVol().add(volume);

        return transp;
    }

    /**
     * Preenche os dados de pagamento
     */
    private Pag preencherPagamento(NotaFiscalDto dto) {
        Pag pag = new Pag();
        Pag.DetPag detPag = new Pag.DetPag();
        detPag.setTPag(dto.getFormaPagamento()); // 01=Dinheiro
        detPag.setVPag(formatar(dto.getValorTotal()));
        pag.getDetPag().add(detPag);
        return pag;
    }

    /**
     * Preenche Informações Adicionais
     */
    private InfAdic preencherInformacoesAdicionais(NotaFiscalDto dto){
        InfAdic infAdic = new InfAdic();
        infAdic.setInfAdFisco(dto.getInformacaoAdicionalFisco());
        return infAdic;
    }

    /**
     * Processa o envio da NF-e (assinatura, validação, envio e retorno)
     */
    private NotaFiscalDto processarEnvioNFe(ConfiguracoesNfe config, TEnviNFe enviNFe, NotaFiscalDto dto) throws NfeException, JAXBException, IOException {

        // 1. Assinar a NFe
        enviNFe = assinarNFe(enviNFe, config);

        // 2. Validar a NFe
        validarNFe(enviNFe, config);

        // 3. Transmitir a NFe
        TRetEnviNFe retorno = transmitirNFe(enviNFe, config);

        // 4. Processar o retorno
        return processarRetorno(enviNFe, retorno, dto);
    }

    /**
     * Assina a NF-e
     */
    private TEnviNFe assinarNFe(TEnviNFe enviNFe, ConfiguracoesNfe config) throws NfeException, JAXBException {
        logger.info("Assinando NF-e, ID Lote: {}", enviNFe.getIdLote());
        String xml = XmlNfeUtil.objectToXml(enviNFe, config.getEncode());
        String xmlAssinado = Assinar.assinaNfe(config, xml, AssinaturaEnum.NFE);
        return XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
    }

    /**
     * Valida a NF-e
     */
    private void validarNFe(TEnviNFe enviNFe, ConfiguracoesNfe config) throws NfeException, JAXBException, IOException {
        logger.info("Validando NF-e, ID Lote: {}", enviNFe.getIdLote());
        String xml = XmlNfeUtil.objectToXml(enviNFe, config.getEncode());

        // Salva temporariamente para análise (opcional)
        Files.write(Paths.get("xml_nota.xml"), xml.getBytes());

        if (!new Validar().isValidXml(config, xml, ServicosEnum.ENVIO)) {
            throw new NfeException("XML inválido segundo schemas da SEFAZ");
        }
    }

    /**
     * Transmite a NF-e para a SEFAZ
     */
    private br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe transmitirNFe(TEnviNFe enviNFe, ConfiguracoesNfe config) throws NfeException {
        logger.info("Enviando NF-e, ID Lote: {}", enviNFe.getIdLote());

        // Verifica status do serviço
        TRetConsStatServ status = consultarStatusServico(config);
        if (!status.getCStat().equals("107")) {
            throw new NfeException("SEFAZ offline - Ativar contingência");
        }

        // Envia a NFe e converte o retorno
        br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe retornoEnviNFe = Nfe.enviarNfe(config, enviNFe, DocumentoEnum.NFE);
        return converterRetornoEnviNFeParaRetEnviNFe(retornoEnviNFe);
    }

    /**
     * Converte o retorno de envio da NFe (enviNFe.TRetEnviNFe) para o formato de retorno esperado (retEnviNFe.TRetEnviNFe)
     */
    private br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe converterRetornoEnviNFeParaRetEnviNFe(br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe retornoEnviNFe) {

        br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe retEnviNFe = new br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe();

        // Copia os campos básicos
        retEnviNFe.setTpAmb(retornoEnviNFe.getTpAmb());
        retEnviNFe.setVerAplic(retornoEnviNFe.getVerAplic());
        retEnviNFe.setCStat(retornoEnviNFe.getCStat());
        retEnviNFe.setXMotivo(retornoEnviNFe.getXMotivo());
        retEnviNFe.setCUF(retornoEnviNFe.getCUF());
        retEnviNFe.setDhRecbto(retornoEnviNFe.getDhRecbto());
        retEnviNFe.setVersao(retornoEnviNFe.getVersao());

        // Trata o infRec se existir
        if (retornoEnviNFe.getInfRec() != null) {
            br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe.InfRec infRec =
                    new br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe.InfRec();
            infRec.setNRec(retornoEnviNFe.getInfRec().getNRec());
            infRec.setTMed(retornoEnviNFe.getInfRec().getTMed());
            retEnviNFe.setInfRec(infRec);
        }

        // Trata o protNFe se existir
        if (retornoEnviNFe.getProtNFe() != null) {
            br.com.swconsultoria.nfe.schema_4.retEnviNFe.TProtNFe protNFe =
                    new br.com.swconsultoria.nfe.schema_4.retEnviNFe.TProtNFe();
            protNFe.setVersao(retornoEnviNFe.getProtNFe().getVersao());

            // Cria o InfProt
            br.com.swconsultoria.nfe.schema_4.retEnviNFe.TProtNFe.InfProt infProt =
                    new br.com.swconsultoria.nfe.schema_4.retEnviNFe.TProtNFe.InfProt();
            infProt.setId(retornoEnviNFe.getProtNFe().getInfProt().getId());
            infProt.setTpAmb(retornoEnviNFe.getProtNFe().getInfProt().getTpAmb());
            infProt.setVerAplic(retornoEnviNFe.getProtNFe().getInfProt().getVerAplic());
            infProt.setChNFe(retornoEnviNFe.getProtNFe().getInfProt().getChNFe());
            infProt.setDhRecbto(retornoEnviNFe.getProtNFe().getInfProt().getDhRecbto());
            infProt.setNProt(retornoEnviNFe.getProtNFe().getInfProt().getNProt());
            infProt.setDigVal(retornoEnviNFe.getProtNFe().getInfProt().getDigVal());
            infProt.setCStat(retornoEnviNFe.getProtNFe().getInfProt().getCStat());
            infProt.setXMotivo(retornoEnviNFe.getProtNFe().getInfProt().getXMotivo());

            protNFe.setInfProt(infProt);
            retEnviNFe.setProtNFe(protNFe);
        }

        return retEnviNFe;
    }

    /**
     * Processa o retorno da SEFAZ
     */
    private NotaFiscalDto processarRetorno(TEnviNFe enviNFe, br.com.swconsultoria.nfe.schema_4.retEnviNFe.TRetEnviNFe retorno, NotaFiscalDto dto)throws NfeException, JAXBException {

        // Atualiza o DTO com os dados do retorno
        dto.setChave(retorno.getProtNFe().getInfProt().getChNFe());
        dto.setCstat(retorno.getProtNFe().getInfProt().getCStat());
        dto.setNumeroProtocolo(retorno.getProtNFe().getInfProt().getNProt());
        dto.setMotivoProtocolo(retorno.getProtNFe().getInfProt().getXMotivo());

        // Gera e salva o XML completo (nfeProc)
        String xml = XmlNfeUtil.criaNfeProc(enviNFe, retorno.getProtNFe());
        salvarXmlNotaFiscal(dto.getChave() + "-proc", xml);

        return dto;
    }

    /**
     * Consulta o status do serviço da SEFAZ
     */
    public TRetConsStatServ consultarStatusServico(ConfiguracoesNfe config) throws NfeException {
        logger.info("Consultando status de serviço da SEFAZ");
        return Nfe.statusServico(config, DocumentoEnum.NFE);
    }

    // Cancela uma NF-e.
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

    //Emite uma Carta de Correção Eletrônica (CC-e) para uma NF-e.
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

    //Inutiliza uma faixa de numeração de NF-e.
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
     * Salva o XML da NF-e no banco de dados
     */
    @Transactional
    private void salvarXmlNotaFiscal(String chaveAcesso, String xml) throws NfeException {
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
     * Gera um ID de lote único
     */
    private String gerarIdLote() {
        return String.format("%015d", System.currentTimeMillis() % 1000000000000000L);
    }

    /**
     * Converte código de ambiente para enum
     */
    private AmbienteEnum converterCodigoParaAmbienteEnum(int codigo) throws NfeException {
        for (AmbienteEnum ambiente : AmbienteEnum.values()) {
            if (Integer.parseInt(ambiente.getCodigo()) == codigo) {
                return ambiente;
            }
        }
        throw new NfeException("Código de ambiente inválido: " + codigo);
    }

    //Consulta o status de uma NF-e.
    public br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TRetConsSitNFe consultarNotaFiscal(String chave, ConfiguracoesNfe config) throws NfeException {
        logger.info("Consultando status da NF-e, chave: {}", chave);
        return Nfe.consultaXml(config, chave, DocumentoEnum.NFE);
    }

    //Consulta e atualiza a sequência de NSU, integrando com ControleNsuService.
    @Transactional
    public BigInteger consultarNSU(String cnpj, String ambiente) throws NfeException {
        logger.info("Consultando NSU para CNPJ: {}, Ambiente: {}", cnpj, ambiente);
        controleNsuService.consultarDocumentos(cnpj, ambiente);
        return controleNsuService.consultarUltimoNSU(cnpj);
    }

    //Envia um evento manual (ex.: manifestação do destinatário, EPEC).
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


    //Extrai a chave de evento do XML (métudo auxiliar).
    private String extrairChaveEvento(String xml) {
        // Implementação simplificada para extrair chNFe do XML
        int index = xml.indexOf("<chNFe>");
        if (index != -1) {
            return xml.substring(index + 7, index + 51);
        }
        return "desconhecido-" + System.currentTimeMillis();
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

}