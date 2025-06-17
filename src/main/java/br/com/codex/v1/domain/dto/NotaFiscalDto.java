package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class NotaFiscalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoUf;
    private String codigoNf;
    private String naturezaOperacao;
    private String localDestino;
    private String modelo;
    private String serie;
    private String numero;
    private LocalDate emissao;
    private String dhSaidaEntrada;
    private String tipo;
    private String indicadorPresenca;
    private Integer indicadorFinal;
    private String razaoSocialEmitente;
    private String nomeFantasiaEmitente;
    private String documentoEmitente;
    private String inscricaoEstadualEmitente;
    private Integer inscricaoEstadualStEmitente;
    private Integer inscricaoMunicipalEmitente;
    private String cnaeEmitente;
    private Integer regimeTributarioEmitente;
    private String cepEmitente;
    private String codigoMunicipioEmitente;
    private String nomeMunicipioEmitente;
    private String bairroEmitente;
    private String telefoneEmitente;
    private String logradouroEmitente;
    private String numeroEnderecoEmitente;
    private String ufEmitente;
    private String razaoSocialDestinatario;
    private String documentoDestinatario;
    private Integer indicadorInscricaoEstadualDestinatario;
    private String inscricaoEstadualDestinatario;
    private String cepDestinatario;
    private String codigoMunicipioDestinatario;
    private String nomeMunicipioDestinatario;
    private String bairroDestinatario;
    private String telefoneDestinatario;
    private String logradouroDestinatario;
    private String numeroEnderecoDestinatario;
    private String ufDestinatario;
    private String emailDestinatario;
    private String codigoPaisDestinatario;
    private String paisDestinatario;
    private BigDecimal valorBaseCalculo;
    private BigDecimal valorIcms;
    private BigDecimal valorIcmsDesonerado;
    private BigDecimal valorFcp;
    private BigDecimal valorBaseCalculoSt;
    private BigDecimal valorSt;
    private BigDecimal valorFcpSt;
    private BigDecimal valorFcpStRetido;
    private BigDecimal valorProdutos;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDesconto;
    private BigDecimal valorIi;
    private BigDecimal valorIpi;
    private BigDecimal valorIpiDevolucao;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorOutros;
    private BigDecimal valorTotal;
    private String modalidadeFrete;
    private String cnpjTransportador;
    private String nomeTransportador;
    private String enderecoTransportador;
    private String municipioTransportador;
    private String numeroFatura;
    private BigDecimal valorOriginalFatura;
    private BigDecimal valorDescontoFatura;
    private BigDecimal valorLiquidoFatura;
    private String informacaoAdicionalFisco;
    private String informacaoAdicionalContribuinte;
    private String chave;
    private String cstat;
    private String numeroProtocolo;
    private String dataHoraProtocolo;
    private String motivoProtocolo;

    /* ------------ RELACIONAMENTO ------------ */
    private List<NotaFiscalItemDto> itens;
    private List<NotaFiscalDuplicatasDto> duplicatas;

    public NotaFiscalDto() {
        super();
    }

    public NotaFiscalDto(NotaFiscal obj) {
        this.id = obj.getId();
        this.codigoUf = obj.getCodigoUf();
        this.codigoNf = obj.getCodigoNf();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.localDestino = obj.getLocalDestino();
        this.modelo = obj.getModelo();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.emissao = obj.getEmissao();
        this.dhSaidaEntrada = obj.getDhSaidaEntrada();
        this.tipo = obj.getTipo();
        this.indicadorPresenca = obj.getIndicadorPresenca();
        this.indicadorFinal = obj.getIndicadorFinal();
        this.razaoSocialEmitente = obj.getRazaoSocialEmitente();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.documentoEmitente = obj.getDocumentoEmitente();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
        this.inscricaoEstadualStEmitente = obj.getInscricaoEstadualStEmitente();
        this.inscricaoMunicipalEmitente = obj.getInscricaoMunicipalEmitente();
        this.cnaeEmitente = obj.getCnaeEmitente();
        this.regimeTributarioEmitente = obj.getRegimeTributarioEmitente();
        this.cepEmitente = obj.getCepEmitente();
        this.codigoMunicipioEmitente = obj.getCodigoMunicipioEmitente();
        this.nomeMunicipioEmitente = obj.getNomeMunicipioEmitente();
        this.bairroEmitente = obj.getBairroEmitente();
        this.telefoneEmitente = obj.getTelefoneEmitente();
        this.logradouroEmitente = obj.getLogradouroEmitente();
        this.numeroEnderecoEmitente = obj.getNumeroEnderecoEmitente();
        this.ufEmitente = obj.getUfEmitente();
        this.razaoSocialDestinatario = obj.getRazaoSocialDestinatario();
        this.documentoDestinatario = obj.getDocumentoDestinatario();
        this.indicadorInscricaoEstadualDestinatario =  obj.getIndicadorInscricaoEstadualDestinatario();
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.cepDestinatario = obj.getCepDestinatario();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.nomeMunicipioDestinatario = obj.getNomeMunicipioDestinatario();
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.numeroEnderecoDestinatario = obj.getNumeroEnderecoDestinatario();
        this.ufDestinatario = obj.getUfDestinatario();
        this.emailDestinatario = obj.getEmailDestinatario();
        this.codigoPaisDestinatario = String.valueOf(obj.getCodigoPaisDestinatario());
        this.paisDestinatario = obj.getPaisDestinatario();
        this.valorBaseCalculo = obj.getValorBaseCalculo();
        this.valorIcms = obj.getValorIcms();
        this.valorIcmsDesonerado = obj.getValorIcmsDesonerado();
        this.valorFcp = obj.getValorFcp();
        this.valorBaseCalculoSt = obj.getValorBaseCalculoSt();
        this.valorSt = obj.getValorSt();
        this.valorFcpSt = obj.getValorFcpSt();
        this.valorFcpStRetido = obj.getValorFcpStRetido();
        this.valorProdutos = obj.getValorProdutos();
        this.valorFrete = obj.getValorFrete();
        this.valorSeguro = obj.getValorSeguro();
        this.valorDesconto = obj.getValorDesconto();
        this.valorIi = obj.getValorIi();
        this.valorIpi = obj.getValorIpi();
        this.valorIpiDevolucao = obj.getValorIpiDevolucao();
        this.valorPis = obj.getValorPis();
        this.valorCofins = obj.getValorCofins();
        this.valorOutros = obj.getValorOutros();
        this.valorTotal = obj.getValorTotal();
        this.modalidadeFrete = obj.getModalidadeFrete();
        this.cnpjTransportador = obj.getCnpjTransportador();
        this.nomeTransportador = obj.getNomeTransportador();
        this.enderecoTransportador = obj.getEnderecoTransportador();
        this.municipioTransportador = obj.getMunicipioTransportador();
        this.numeroFatura = obj.getNumeroFatura();
        this.valorOriginalFatura = obj.getValorOriginalFatura();
        this.valorDescontoFatura = obj.getValorDescontoFatura();
        this.valorLiquidoFatura = obj.getValorLiquidoFatura();
        this.informacaoAdicionalFisco = obj.getInformacaoAdicionalFisco();
        this.informacaoAdicionalContribuinte = obj.getInformacaoAdicionalContribuinte();
        this.chave = obj.getChave();
        this.cstat = obj.getCstat();
        this.numeroProtocolo = obj.getNumeroProtocolo();
        this.dataHoraProtocolo = obj.getDataHoraProtocolo();
        this.motivoProtocolo = obj.getMotivoProtocolo();

        // Convertendo itens da nota fiscal para DTO
        this.itens = obj.getItens() != null ? obj.getItens().stream().map(NotaFiscalItemDto::new).toList() : null;

        // Convertendo duplicatas da nota fiscal para DTO
        this.duplicatas = obj.getDuplicatas() != null ? obj.getDuplicatas().stream().map(NotaFiscalDuplicatasDto::new).toList() : null;
    }
}