package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.NotaFiscal;
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

    private	String	bairroDestinatario;
    private	String	bairroEmitente;
    private	String	cepDestinatario;
    private	String	cepEmitente;
    private	String	chave;
    private	String	cnaeEmitente;
    private	String	cnpjTransportador;
    private	String	codigoMunicipioDestinatario;
    private	String	codigoMunicipioEmitente;
    private	String	codigoNf;
    private	String	codigoPaisDestinatario;
    private	String	codigoUf;
    private Integer consumidorFinal;
    private	String	cstat;
    private	String	dataHoraProtocolo;
    private	String	dhSaidaEntrada;
    private	String	documentoDestinatario;
    private	String	documentoEmitente;
    private	String	emailDestinatario;
    private	LocalDate emissao;
    private	String	enderecoTransportador;
    private Integer finalidadeEmissao;
    private String formaPagamento;
    private	Long	id;
    private	Integer	indicadorFinal;
    private String  indicadorIntermediario;
    private	Integer	indicadorInscricaoEstadualDestinatario;
    private	String	indicadorPresenca;
    private	String	informacaoAdicionalContribuinte;
    private	String	informacaoAdicionalFisco;
    private	String	inscricaoEstadualDestinatario;
    private	String	inscricaoEstadualEmitente;
    private	Integer	inscricaoEstadualStEmitente;
    private	Integer	inscricaoMunicipalEmitente;
    private	String	localDestino;
    private	String	logradouroDestinatario;
    private	String	logradouroEmitente;
    private	String	modalidadeFrete;
    private	String	modelo;
    private	String	motivoProtocolo;
    private	String	municipioTransportador;
    private	String	naturezaOperacao;
    private	String	nomeFantasiaEmitente;
    private	String	nomeMunicipioDestinatario;
    private	String	nomeMunicipioEmitente;
    private	String	nomeTransportador;
    private	String	numero;
    private	String	numeroEnderecoDestinatario;
    private	String	numeroEnderecoEmitente;
    private	String	numeroFatura;
    private	String	numeroProtocolo;
    private	String	paisDestinatario;
    private String  presencaComprador;
    private	String	razaoSocialDestinatario;
    private	String	razaoSocialEmitente;
    private	Integer	regimeTributarioEmitente;
    private	String	serie;
    private	String	telefoneDestinatario;
    private	String	telefoneEmitente;
    private	String	tipo;
    private String tipoAmbiente;
    private	String	ufDestinatario;
    private	String	ufEmitente;
    private	BigDecimal	valorBaseCalculo;
    private	BigDecimal	valorBaseCalculoSt;
    private	BigDecimal	valorCofins;
    private	BigDecimal	valorDesconto;
    private	BigDecimal	valorDescontoFatura;
    private	BigDecimal	valorFcp;
    private	BigDecimal	valorFcpSt;
    private	BigDecimal	valorFcpStRetido;
    private	BigDecimal	valorFrete;
    private	BigDecimal	valorIcms;
    private	BigDecimal	valorIcmsDesonerado;
    private	BigDecimal	valorIi;
    private	BigDecimal	valorIpi;
    private	BigDecimal	valorIpiDevolucao;
    private	BigDecimal	valorLiquidoFatura;
    private	BigDecimal	valorOriginalFatura;
    private	BigDecimal	valorOutros;
    private	BigDecimal	valorPagamento;
    private	BigDecimal	valorPis;
    private	BigDecimal	valorProdutos;
    private	BigDecimal	valorSeguro;
    private	BigDecimal	valorSt;
    private	BigDecimal	valorTotal;

    /* ------------ RELACIONAMENTO ------------ */
    private List<NotaFiscalItemDto> itens;
    private List<NotaFiscalDuplicatasDto> duplicatas;

    public NotaFiscalDto() {
        super();
    }

    public NotaFiscalDto(NotaFiscal obj) {
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.bairroEmitente = obj.getBairroEmitente();
        this.cepDestinatario = obj.getCepDestinatario();
        this.cepEmitente = obj.getCepEmitente();
        this.chave = obj.getChave();
        this.cnaeEmitente = obj.getCnaeEmitente();
        this.cnpjTransportador = obj.getCnpjTransportador();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.codigoMunicipioEmitente = obj.getCodigoMunicipioEmitente();
        this.codigoNf = obj.getCodigoNf();
        this.codigoPaisDestinatario = String.valueOf(obj.getCodigoPaisDestinatario());
        this.codigoUf = obj.getCodigoUf();
        this.consumidorFinal = obj.getConsumidorFinal();
        this.cstat = obj.getCstat();
        this.dataHoraProtocolo = obj.getDataHoraProtocolo();
        this.dhSaidaEntrada = obj.getDhSaidaEntrada();
        this.documentoDestinatario = obj.getDocumentoDestinatario();
        this.documentoEmitente = obj.getDocumentoEmitente();
        this.emailDestinatario = obj.getEmailDestinatario();
        this.emissao = obj.getEmissao();
        this.enderecoTransportador = obj.getEnderecoTransportador();
        this.finalidadeEmissao = obj.getFinalidadeEmissao();
        this.formaPagamento = obj.getFormaPagamento();
        this.id = obj.getId();
        this.indicadorFinal = obj.getIndicadorFinal();
        this.indicadorIntermediario = obj.getIndicadorIntermediario();
        this.indicadorInscricaoEstadualDestinatario = obj.getIndicadorInscricaoEstadualDestinatario();
        this.indicadorPresenca = obj.getIndicadorPresenca();
        this.informacaoAdicionalContribuinte = obj.getInformacaoAdicionalContribuinte();
        this.informacaoAdicionalFisco = obj.getInformacaoAdicionalFisco();
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
        this.inscricaoEstadualStEmitente = obj.getInscricaoEstadualStEmitente();
        this.inscricaoMunicipalEmitente = obj.getInscricaoMunicipalEmitente();
        this.localDestino = obj.getLocalDestino();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.logradouroEmitente = obj.getLogradouroEmitente();
        this.modelo = obj.getModelo();
        this.modalidadeFrete = obj.getModalidadeFrete();
        this.motivoProtocolo = obj.getMotivoProtocolo();
        this.municipioTransportador = obj.getMunicipioTransportador();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.nomeMunicipioDestinatario = obj.getNomeMunicipioDestinatario();
        this.nomeMunicipioEmitente = obj.getNomeMunicipioEmitente();
        this.nomeTransportador = obj.getNomeTransportador();
        this.numero = obj.getNumero();
        this.numeroEnderecoDestinatario = obj.getNumeroEnderecoDestinatario();
        this.numeroEnderecoEmitente = obj.getNumeroEnderecoEmitente();
        this.numeroFatura = obj.getNumeroFatura();
        this.numeroProtocolo = obj.getNumeroProtocolo();
        this.paisDestinatario = obj.getPaisDestinatario();
        this.presencaComprador = obj.getPresencaComprador();
        this.razaoSocialDestinatario = obj.getRazaoSocialDestinatario();
        this.razaoSocialEmitente = obj.getRazaoSocialEmitente();
        this.regimeTributarioEmitente = obj.getRegimeTributarioEmitente();
        this.serie = obj.getSerie();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.telefoneEmitente = obj.getTelefoneEmitente();
        this.tipo = obj.getTipo();
        this.tipoAmbiente = obj.getTipoAmbiente();
        this.ufDestinatario = obj.getUfDestinatario();
        this.ufEmitente = obj.getUfEmitente();
        this.valorBaseCalculo = obj.getValorBaseCalculo();
        this.valorBaseCalculoSt = obj.getValorBaseCalculoSt();
        this.valorCofins = obj.getValorCofins();
        this.valorDesconto = obj.getValorDesconto();
        this.valorDescontoFatura = obj.getValorDescontoFatura();
        this.valorFcp = obj.getValorFcp();
        this.valorFcpSt = obj.getValorFcpSt();
        this.valorFcpStRetido = obj.getValorFcpStRetido();
        this.valorFrete = obj.getValorFrete();
        this.valorIcms = obj.getValorIcms();
        this.valorIcmsDesonerado = obj.getValorIcmsDesonerado();
        this.valorIi = obj.getValorIi();
        this.valorIpi = obj.getValorIpi();
        this.valorIpiDevolucao = obj.getValorIpiDevolucao();
        this.valorLiquidoFatura = obj.getValorLiquidoFatura();
        this.valorOriginalFatura = obj.getValorOriginalFatura();
        this.valorOutros = obj.getValorOutros();
        this.valorPagamento = obj.getValorPagamento();
        this.valorPis = obj.getValorPis();
        this.valorProdutos = obj.getValorProdutos();
        this.valorSeguro = obj.getValorSeguro();
        this.valorSt = obj.getValorSt();
        this.valorTotal = obj.getValorTotal();

        // Convertendo itens da nota fiscal para DTO
        this.itens = obj.getItens() != null ? obj.getItens().stream().map(NotaFiscalItemDto::new).toList() : null;

        // Convertendo duplicatas da nota fiscal para DTO
        this.duplicatas = obj.getDuplicatas() != null ? obj.getDuplicatas().stream().map(NotaFiscalDuplicatasDto::new).toList() : null;
    }
}