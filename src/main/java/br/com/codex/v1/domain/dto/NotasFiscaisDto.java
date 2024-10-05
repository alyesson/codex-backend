package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.NotasFiscais;

import java.math.BigDecimal;
import java.sql.Date;

public class NotasFiscaisDto {

    private Integer id;
    private String xml;
    private String codigoUf;
    private String codigoNf;
    private String naturezaOperacao;
    private String modelo;
    private String serie;
    private String numero;
    private String emissao;
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
    private Date dataImportacao;

    public NotasFiscaisDto() {
        super();
    }

    public NotasFiscaisDto(NotasFiscais obj) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(String codigoUf) {
        this.codigoUf = codigoUf;
    }

    public String getCodigoNf() {
        return codigoNf;
    }

    public void setCodigoNf(String codigoNf) {
        this.codigoNf = codigoNf;
    }

    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }

    public String getDhSaidaEntrada() {
        return dhSaidaEntrada;
    }

    public void setDhSaidaEntrada(String dhSaidaEntrada) {
        this.dhSaidaEntrada = dhSaidaEntrada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIndicadorPresenca() {
        return indicadorPresenca;
    }

    public void setIndicadorPresenca(String indicadorPresenca) {
        this.indicadorPresenca = indicadorPresenca;
    }

    public String getRazaoSocialEmitente() {
        return razaoSocialEmitente;
    }

    public void setRazaoSocialEmitente(String razaoSocialEmitente) {
        this.razaoSocialEmitente = razaoSocialEmitente;
    }

    public String getNomeFantasiaEmitente() {
        return nomeFantasiaEmitente;
    }

    public void setNomeFantasiaEmitente(String nomeFantasiaEmitente) {
        this.nomeFantasiaEmitente = nomeFantasiaEmitente;
    }

    public String getDocumentoEmitente() {
        return documentoEmitente;
    }

    public void setDocumentoEmitente(String documentoEmitente) {
        this.documentoEmitente = documentoEmitente;
    }

    public String getInscricaoEstadualEmitente() {
        return inscricaoEstadualEmitente;
    }

    public void setInscricaoEstadualEmitente(String inscricaoEstadualEmitente) {
        this.inscricaoEstadualEmitente = inscricaoEstadualEmitente;
    }

    public String getCepEmitente() {
        return cepEmitente;
    }

    public void setCepEmitente(String cepEmitente) {
        this.cepEmitente = cepEmitente;
    }

    public String getCodigoMunicipioEmitente() {
        return codigoMunicipioEmitente;
    }

    public void setCodigoMunicipioEmitente(String codigoMunicipioEmitente) {
        this.codigoMunicipioEmitente = codigoMunicipioEmitente;
    }

    public String getNomeMunicipioEmitente() {
        return nomeMunicipioEmitente;
    }

    public void setNomeMunicipioEmitente(String nomeMunicipioEmitente) {
        this.nomeMunicipioEmitente = nomeMunicipioEmitente;
    }

    public String getBairroEmitente() {
        return bairroEmitente;
    }

    public void setBairroEmitente(String bairroEmitente) {
        this.bairroEmitente = bairroEmitente;
    }

    public String getTelefoneEmitente() {
        return telefoneEmitente;
    }

    public void setTelefoneEmitente(String telefoneEmitente) {
        this.telefoneEmitente = telefoneEmitente;
    }

    public String getLogradouroEmitente() {
        return logradouroEmitente;
    }

    public void setLogradouroEmitente(String logradouroEmitente) {
        this.logradouroEmitente = logradouroEmitente;
    }

    public String getNumeroEnderecoEmitente() {
        return numeroEnderecoEmitente;
    }

    public void setNumeroEnderecoEmitente(String numeroEnderecoEmitente) {
        this.numeroEnderecoEmitente = numeroEnderecoEmitente;
    }

    public String getUfEmitente() {
        return ufEmitente;
    }

    public void setUfEmitente(String ufEmitente) {
        this.ufEmitente = ufEmitente;
    }

    public String getRazaoSocialDestinatario() {
        return razaoSocialDestinatario;
    }

    public void setRazaoSocialDestinatario(String razaoSocialDestinatario) {
        this.razaoSocialDestinatario = razaoSocialDestinatario;
    }

    public String getDocumentoDestinatario() {
        return documentoDestinatario;
    }

    public void setDocumentoDestinatario(String documentoDestinatario) {
        this.documentoDestinatario = documentoDestinatario;
    }

    public String getInscricaoEstadualDestinatario() {
        return inscricaoEstadualDestinatario;
    }

    public void setInscricaoEstadualDestinatario(String inscricaoEstadualDestinatario) {
        this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
    }

    public String getCepDestinatario() {
        return cepDestinatario;
    }

    public void setCepDestinatario(String cepDestinatario) {
        this.cepDestinatario = cepDestinatario;
    }

    public String getCodigoMunicipioDestinatario() {
        return codigoMunicipioDestinatario;
    }

    public void setCodigoMunicipioDestinatario(String codigoMunicipioDestinatario) {
        this.codigoMunicipioDestinatario = codigoMunicipioDestinatario;
    }

    public String getNomeMunicipioDestinatario() {
        return nomeMunicipioDestinatario;
    }

    public void setNomeMunicipioDestinatario(String nomeMunicipioDestinatario) {
        this.nomeMunicipioDestinatario = nomeMunicipioDestinatario;
    }

    public String getBairroDestinatario() {
        return bairroDestinatario;
    }

    public void setBairroDestinatario(String bairroDestinatario) {
        this.bairroDestinatario = bairroDestinatario;
    }

    public String getTelefoneDestinatario() {
        return telefoneDestinatario;
    }

    public void setTelefoneDestinatario(String telefoneDestinatario) {
        this.telefoneDestinatario = telefoneDestinatario;
    }

    public String getLogradouroDestinatario() {
        return logradouroDestinatario;
    }

    public void setLogradouroDestinatario(String logradouroDestinatario) {
        this.logradouroDestinatario = logradouroDestinatario;
    }

    public String getNumeroEnderecoDestinatario() {
        return numeroEnderecoDestinatario;
    }

    public void setNumeroEnderecoDestinatario(String numeroEnderecoDestinatario) {
        this.numeroEnderecoDestinatario = numeroEnderecoDestinatario;
    }

    public String getUfDestinatario() {
        return ufDestinatario;
    }

    public void setUfDestinatario(String ufDestinatario) {
        this.ufDestinatario = ufDestinatario;
    }

    public BigDecimal getValorBaseCalculo() {
        return valorBaseCalculo;
    }

    public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
        this.valorBaseCalculo = valorBaseCalculo;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public BigDecimal getValorIcmsDesonerado() {
        return valorIcmsDesonerado;
    }

    public void setValorIcmsDesonerado(BigDecimal valorIcmsDesonerado) {
        this.valorIcmsDesonerado = valorIcmsDesonerado;
    }

    public BigDecimal getValorFcp() {
        return valorFcp;
    }

    public void setValorFcp(BigDecimal valorFcp) {
        this.valorFcp = valorFcp;
    }

    public BigDecimal getValorBaseCalculoSt() {
        return valorBaseCalculoSt;
    }

    public void setValorBaseCalculoSt(BigDecimal valorBaseCalculoSt) {
        this.valorBaseCalculoSt = valorBaseCalculoSt;
    }

    public BigDecimal getValorSt() {
        return valorSt;
    }

    public void setValorSt(BigDecimal valorSt) {
        this.valorSt = valorSt;
    }

    public BigDecimal getValorFcpSt() {
        return valorFcpSt;
    }

    public void setValorFcpSt(BigDecimal valorFcpSt) {
        this.valorFcpSt = valorFcpSt;
    }

    public BigDecimal getValorFcpStRetido() {
        return valorFcpStRetido;
    }

    public void setValorFcpStRetido(BigDecimal valorFcpStRetido) {
        this.valorFcpStRetido = valorFcpStRetido;
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public void setValorProdutos(BigDecimal valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }

    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorIi() {
        return valorIi;
    }

    public void setValorIi(BigDecimal valorIi) {
        this.valorIi = valorIi;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public BigDecimal getValorIpiDevolucao() {
        return valorIpiDevolucao;
    }

    public void setValorIpiDevolucao(BigDecimal valorIpiDevolucao) {
        this.valorIpiDevolucao = valorIpiDevolucao;
    }

    public BigDecimal getValorPis() {
        return valorPis;
    }

    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }

    public BigDecimal getValorCofins() {
        return valorCofins;
    }

    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }

    public BigDecimal getValorOutros() {
        return valorOutros;
    }

    public void setValorOutros(BigDecimal valorOutros) {
        this.valorOutros = valorOutros;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getModalidadeFrete() {
        return modalidadeFrete;
    }

    public void setModalidadeFrete(String modalidadeFrete) {
        this.modalidadeFrete = modalidadeFrete;
    }

    public String getCnpjTransportador() {
        return cnpjTransportador;
    }

    public void setCnpjTransportador(String cnpjTransportador) {
        this.cnpjTransportador = cnpjTransportador;
    }

    public String getNomeTransportador() {
        return nomeTransportador;
    }

    public void setNomeTransportador(String nomeTransportador) {
        this.nomeTransportador = nomeTransportador;
    }

    public String getEnderecoTransportador() {
        return enderecoTransportador;
    }

    public void setEnderecoTransportador(String enderecoTransportador) {
        this.enderecoTransportador = enderecoTransportador;
    }

    public String getMunicipioTransportador() {
        return municipioTransportador;
    }

    public void setMunicipioTransportador(String municipioTransportador) {
        this.municipioTransportador = municipioTransportador;
    }

    public String getNumeroFatura() {
        return numeroFatura;
    }

    public void setNumeroFatura(String numeroFatura) {
        this.numeroFatura = numeroFatura;
    }

    public BigDecimal getValorOriginalFatura() {
        return valorOriginalFatura;
    }

    public void setValorOriginalFatura(BigDecimal valorOriginalFatura) {
        this.valorOriginalFatura = valorOriginalFatura;
    }

    public BigDecimal getValorDescontoFatura() {
        return valorDescontoFatura;
    }

    public void setValorDescontoFatura(BigDecimal valorDescontoFatura) {
        this.valorDescontoFatura = valorDescontoFatura;
    }

    public BigDecimal getValorLiquidoFatura() {
        return valorLiquidoFatura;
    }

    public void setValorLiquidoFatura(BigDecimal valorLiquidoFatura) {
        this.valorLiquidoFatura = valorLiquidoFatura;
    }

    public String getInformacaoAdicionalFisco() {
        return informacaoAdicionalFisco;
    }

    public void setInformacaoAdicionalFisco(String informacaoAdicionalFisco) {
        this.informacaoAdicionalFisco = informacaoAdicionalFisco;
    }

    public String getInformacaoAdicionalContribuinte() {
        return informacaoAdicionalContribuinte;
    }

    public void setInformacaoAdicionalContribuinte(String informacaoAdicionalContribuinte) {
        this.informacaoAdicionalContribuinte = informacaoAdicionalContribuinte;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getCstat() {
        return cstat;
    }

    public void setCstat(String cstat) {
        this.cstat = cstat;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public String getDataHoraProtocolo() {
        return dataHoraProtocolo;
    }

    public void setDataHoraProtocolo(String dataHoraProtocolo) {
        this.dataHoraProtocolo = dataHoraProtocolo;
    }

    public String getMotivoProtocolo() {
        return motivoProtocolo;
    }

    public void setMotivoProtocolo(String motivoProtocolo) {
        this.motivoProtocolo = motivoProtocolo;
    }

    public Date getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(Date dataImportacao) {
        this.dataImportacao = dataImportacao;
    }
}
