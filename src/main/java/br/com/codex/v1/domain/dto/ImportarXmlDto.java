package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.ImportarXml;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class ImportarXmlDto {

    private Long id;
    private String xml;
    private String codigoUf;
    private String codigoNf;
    private String naturezaOperacao;
    private String modelo;
    private String serie;
    private String numero;
    private LocalDate emissao;
    private String dhSaidaEntrada;
    private String tipo;
    private String indicadorPresenca;
    private String razaoSocialEmitente;
    private String nomeFantasiaEmitente;
    private String documentoEmitente;
    private String inscricaoEstadualEmitente;
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
    private String inscricaoEstadualDestinatario;
    private String cepDestinatario;
    private String codigoMunicipioDestinatario;
    private String nomeMunicipioDestinatario;
    private String bairroDestinatario;
    private String telefoneDestinatario;
    private String logradouroDestinatario;
    private String numeroEnderecoDestinatario;
    private String ufDestinatario;
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
    private LocalDate dataImportacao;

    public ImportarXmlDto() {
        super();
    }

    public ImportarXmlDto(ImportarXml obj) {
        this.id = obj.getId();
        this.xml = obj.getXml();
        this.codigoUf = obj.getCodigoUf();
        this.codigoNf = obj.getCodigoNf();
        this.naturezaOperacao = obj.getNaturezaOperacao();
        this.modelo = obj.getModelo();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.emissao = obj.getEmissao();
        this.dhSaidaEntrada = obj.getDhSaidaEntrada();
        this.tipo = obj.getTipo();
        this.indicadorPresenca = obj.getIndicadorPresenca();
        this.razaoSocialEmitente = obj.getRazaoSocialEmitente();
        this.nomeFantasiaEmitente = obj.getNomeFantasiaEmitente();
        this.documentoEmitente = obj.getDocumentoEmitente();
        this.inscricaoEstadualEmitente = obj.getInscricaoEstadualEmitente();
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
        this.inscricaoEstadualDestinatario = obj.getInscricaoEstadualDestinatario();
        this.cepDestinatario = obj.getCepDestinatario();
        this.codigoMunicipioDestinatario = obj.getCodigoMunicipioDestinatario();
        this.nomeMunicipioDestinatario = obj.getNomeMunicipioDestinatario();
        this.bairroDestinatario = obj.getBairroDestinatario();
        this.telefoneDestinatario = obj.getTelefoneDestinatario();
        this.logradouroDestinatario = obj.getLogradouroDestinatario();
        this.numeroEnderecoDestinatario = obj.getNumeroEnderecoDestinatario();
        this.ufDestinatario = obj.getUfDestinatario();
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
        this.dataImportacao = obj.getDataImportacao();
    }

    public void setRazaoSocialEmitente(String razaoSocialEmitente) {
        this.razaoSocialEmitente = capitalizarPalavras(razaoSocialEmitente);
    }

    public void setNomeFantasiaEmitente(String nomeFantasiaEmitente) {
        this.nomeFantasiaEmitente = capitalizarPalavras(nomeFantasiaEmitente);
    }

    public void setNomeMunicipioEmitente(String nomeMunicipioEmitente) {
        this.nomeMunicipioEmitente = capitalizarPalavras(nomeMunicipioEmitente);
    }

    public void setBairroEmitente(String bairroEmitente) {
        this.bairroEmitente = capitalizarPalavras(bairroEmitente);
    }

    public void setLogradouroEmitente(String logradouroEmitente) {
        this.logradouroEmitente = capitalizarPalavras(logradouroEmitente);
    }

    public void setRazaoSocialDestinatario(String razaoSocialDestinatario) {
        this.razaoSocialDestinatario = capitalizarPalavras(razaoSocialDestinatario);
    }

    public void setNomeMunicipioDestinatario(String nomeMunicipioDestinatario) {
        this.nomeMunicipioDestinatario = capitalizarPalavras(nomeMunicipioDestinatario);
    }

    public void setBairroDestinatario(String bairroDestinatario) {
        this.bairroDestinatario = capitalizarPalavras(bairroDestinatario);
    }

    public void setLogradouroDestinatario(String logradouroDestinatario) {
        this.logradouroDestinatario = capitalizarPalavras(logradouroDestinatario);
    }

    public void setNomeTransportador(String nomeTransportador) {
        this.nomeTransportador = capitalizarPalavras(nomeTransportador);
    }

    public void setEnderecoTransportador(String enderecoTransportador) {
        this.enderecoTransportador = capitalizarPalavras(enderecoTransportador);
    }

    public void setMunicipioTransportador(String municipioTransportador) {
        this.municipioTransportador = capitalizarPalavras(municipioTransportador);
    }

    public void setInformacaoAdicionalFisco(String informacaoAdicionalFisco) {
        this.informacaoAdicionalFisco = capitalizarPalavras(informacaoAdicionalFisco);
    }

    public void setInformacaoAdicionalContribuinte(String informacaoAdicionalContribuinte) {
        this.informacaoAdicionalContribuinte = capitalizarPalavras(informacaoAdicionalContribuinte);
    }
}
