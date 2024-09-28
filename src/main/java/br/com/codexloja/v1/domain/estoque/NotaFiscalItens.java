package br.com.codexloja.v1.domain.estoque;

import br.com.codexloja.v1.domain.dto.NotaFiscalItensDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class NotaFiscalItens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numeroNotaFiscal;
    private String nomeProduto;
    private String codigoProduto;
    private String ncmSh;
    private String cfop;
    private String item;
    private String unidadeComercial;
    private BigDecimal quantidadeComercial;
    private BigDecimal valorUnitarioComercial;
    private BigDecimal valorTotalProdutos;
    private String unidadeTributacao;
    private BigDecimal quantidadeTributacao;
    private BigDecimal valorUnitarioTributacao;
    private BigDecimal valorDesconto;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorOutro;
    private String informacaoAdicional;
    private String validadeLote;
    private String fabricacaoLote;
    private String numeroLote;
    private String cstIcms;
    private String modBc;
    private String origIcms;
    private BigDecimal aliqIcms;
    private BigDecimal bcIcms;
    private BigDecimal valorIcms;
    private BigDecimal aliqFcp;
    private BigDecimal valorFcp;
    private String modBcSt;
    private String motDesIcms;
    private BigDecimal valorIcmsDesonerado;
    private BigDecimal aliqFcpSt;
    private BigDecimal aliqIcmsSt;
    private BigDecimal percentMargemIcmsSt;
    private BigDecimal percentRedBc;
    private BigDecimal percentRedBcSt;
    private BigDecimal valorIcmsSt;
    private BigDecimal valorFcpSt;
    private BigDecimal bcIcmsSt;
    private BigDecimal bcFcp;
    private BigDecimal bcFcpSt;
    private BigDecimal aliqIcmsDiferido;
    private BigDecimal valorIcmsDiferido;
    private BigDecimal valorIcmsOperacao;
    private BigDecimal aliqFcpStRetido;
    private BigDecimal aliqIcmsEfetivo;
    private BigDecimal percentRedBcEfetivo;
    private BigDecimal bcIcmsEfetivo;
    private BigDecimal bcFcpStRetido;
    private BigDecimal valorIcmsEfetivo;
    private BigDecimal valorIcmsSubstituto;
    private BigDecimal valorFcpStRetido;
    private String csoSn;
    private BigDecimal pstIcms;
    private BigDecimal aliqCredSn;
    private BigDecimal valorCredIcmsSn;
    private BigDecimal bcIcmsStRetido;
    private BigDecimal bcIcmsStDestino;
    private BigDecimal valorIcmsStRetido;
    private BigDecimal valorIcmsStDestino;
    private BigDecimal percentBcOperacao;
    private String ufIcmsSt;
    private String cstPis;
    private BigDecimal aliqPis;
    private BigDecimal bcPis;
    private BigDecimal valorPis;
    private String cstCofins;
    private BigDecimal aliqCofins;
    private BigDecimal quantVendidaCofins;
    private BigDecimal valorAliqCofinsRs;
    private BigDecimal bcCofins;
    private BigDecimal valorCofins;
    private String enquadramentoIpi;
    private String cnpjProdIpi;
    private String codigoSeloIpi;
    private BigDecimal quantidadeSeloIpi;
    private String cstIpi;
    private BigDecimal aliqIpi;
    private BigDecimal quantUniIpi;
    private BigDecimal bcIpi;
    private BigDecimal valorIpi;
    private BigDecimal valorUniIpi;

    public NotaFiscalItens() {
        super();
    }

    public NotaFiscalItens(Integer id, String numeroNotaFiscal, String nomeProduto, String codigoProduto, String ncmSh, String cfop, String item, String unidadeComercial, BigDecimal quantidadeComercial, BigDecimal valorUnitarioComercial, BigDecimal valorTotalProdutos, String unidadeTributacao, BigDecimal quantidadeTributacao, BigDecimal valorUnitarioTributacao, BigDecimal valorDesconto, BigDecimal valorFrete, BigDecimal valorSeguro, BigDecimal valorOutro, String informacaoAdicional, String validadeLote, String fabricacaoLote, String numeroLote, String cstIcms, String modBc, String origIcms, BigDecimal aliqIcms, BigDecimal bcIcms, BigDecimal valorIcms, BigDecimal aliqFcp, BigDecimal valorFcp, String modBcSt, String motDesIcms, BigDecimal valorIcmsDesonerado, BigDecimal aliqFcpSt, BigDecimal aliqIcmsSt, BigDecimal percentMargemIcmsSt, BigDecimal percentRedBc, BigDecimal percentRedBcSt, BigDecimal valorIcmsSt, BigDecimal valorFcpSt, BigDecimal bcIcmsSt, BigDecimal bcFcp, BigDecimal bcFcpSt, BigDecimal aliqIcmsDiferido, BigDecimal valorIcmsDiferido, BigDecimal valorIcmsOperacao, BigDecimal aliqFcpStRetido, BigDecimal aliqIcmsEfetivo, BigDecimal percentRedBcEfetivo, BigDecimal bcIcmsEfetivo, BigDecimal bcFcpStRetido, BigDecimal valorIcmsEfetivo, BigDecimal valorIcmsSubstituto, BigDecimal valorFcpStRetido, String csoSn, BigDecimal pstIcms, BigDecimal aliqCredSn, BigDecimal valorCredIcmsSn, BigDecimal bcIcmsStRetido, BigDecimal bcIcmsStDestino, BigDecimal valorIcmsStRetido, BigDecimal valorIcmsStDestino, BigDecimal percentBcOperacao, String ufIcmsSt, String cstPis, BigDecimal aliqPis, BigDecimal bcPis, BigDecimal valorPis, String cstCofins, BigDecimal aliqCofins, BigDecimal quantVendidaCofins, BigDecimal valorAliqCofinsRs, BigDecimal bcCofins, BigDecimal valorCofins, String enquadramentoIpi, String cnpjProdIpi, String codigoSeloIpi, BigDecimal quantidadeSeloIpi, String cstIpi, BigDecimal aliqIpi, BigDecimal quantUniIpi, BigDecimal bcIpi, BigDecimal valorIpi, BigDecimal valorUniIpi) {
        this.id = id;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.ncmSh = ncmSh;
        this.cfop = cfop;
        this.item = item;
        this.unidadeComercial = unidadeComercial;
        this.quantidadeComercial = quantidadeComercial;
        this.valorUnitarioComercial = valorUnitarioComercial;
        this.valorTotalProdutos = valorTotalProdutos;
        this.unidadeTributacao = unidadeTributacao;
        this.quantidadeTributacao = quantidadeTributacao;
        this.valorUnitarioTributacao = valorUnitarioTributacao;
        this.valorDesconto = valorDesconto;
        this.valorFrete = valorFrete;
        this.valorSeguro = valorSeguro;
        this.valorOutro = valorOutro;
        this.informacaoAdicional = informacaoAdicional;
        this.validadeLote = validadeLote;
        this.fabricacaoLote = fabricacaoLote;
        this.numeroLote = numeroLote;
        this.cstIcms = cstIcms;
        this.modBc = modBc;
        this.origIcms = origIcms;
        this.aliqIcms = aliqIcms;
        this.bcIcms = bcIcms;
        this.valorIcms = valorIcms;
        this.aliqFcp = aliqFcp;
        this.valorFcp = valorFcp;
        this.modBcSt = modBcSt;
        this.motDesIcms = motDesIcms;
        this.valorIcmsDesonerado = valorIcmsDesonerado;
        this.aliqFcpSt = aliqFcpSt;
        this.aliqIcmsSt = aliqIcmsSt;
        this.percentMargemIcmsSt = percentMargemIcmsSt;
        this.percentRedBc = percentRedBc;
        this.percentRedBcSt = percentRedBcSt;
        this.valorIcmsSt = valorIcmsSt;
        this.valorFcpSt = valorFcpSt;
        this.bcIcmsSt = bcIcmsSt;
        this.bcFcp = bcFcp;
        this.bcFcpSt = bcFcpSt;
        this.aliqIcmsDiferido = aliqIcmsDiferido;
        this.valorIcmsDiferido = valorIcmsDiferido;
        this.valorIcmsOperacao = valorIcmsOperacao;
        this.aliqFcpStRetido = aliqFcpStRetido;
        this.aliqIcmsEfetivo = aliqIcmsEfetivo;
        this.percentRedBcEfetivo = percentRedBcEfetivo;
        this.bcIcmsEfetivo = bcIcmsEfetivo;
        this.bcFcpStRetido = bcFcpStRetido;
        this.valorIcmsEfetivo = valorIcmsEfetivo;
        this.valorIcmsSubstituto = valorIcmsSubstituto;
        this.valorFcpStRetido = valorFcpStRetido;
        this.csoSn = csoSn;
        this.pstIcms = pstIcms;
        this.aliqCredSn = aliqCredSn;
        this.valorCredIcmsSn = valorCredIcmsSn;
        this.bcIcmsStRetido = bcIcmsStRetido;
        this.bcIcmsStDestino = bcIcmsStDestino;
        this.valorIcmsStRetido = valorIcmsStRetido;
        this.valorIcmsStDestino = valorIcmsStDestino;
        this.percentBcOperacao = percentBcOperacao;
        this.ufIcmsSt = ufIcmsSt;
        this.cstPis = cstPis;
        this.aliqPis = aliqPis;
        this.bcPis = bcPis;
        this.valorPis = valorPis;
        this.cstCofins = cstCofins;
        this.aliqCofins = aliqCofins;
        this.quantVendidaCofins = quantVendidaCofins;
        this.valorAliqCofinsRs = valorAliqCofinsRs;
        this.bcCofins = bcCofins;
        this.valorCofins = valorCofins;
        this.enquadramentoIpi = enquadramentoIpi;
        this.cnpjProdIpi = cnpjProdIpi;
        this.codigoSeloIpi = codigoSeloIpi;
        this.quantidadeSeloIpi = quantidadeSeloIpi;
        this.cstIpi = cstIpi;
        this.aliqIpi = aliqIpi;
        this.quantUniIpi = quantUniIpi;
        this.bcIpi = bcIpi;
        this.valorIpi = valorIpi;
        this.valorUniIpi = valorUniIpi;
    }

    public NotaFiscalItens(NotaFiscalItensDto obj) {
        this.id = obj.getId();
        this.numeroNotaFiscal = obj.getNumeroNotaFiscal();
        this.nomeProduto = obj.getNomeProduto();
        this.codigoProduto = obj.getCodigoProduto();
        this.ncmSh = obj.getNcmSh();
        this.cfop = obj.getCfop();
        this.item = obj.getItem();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.quantidadeComercial = obj.getQuantidadeComercial();
        this.valorUnitarioComercial = obj.getValorUnitarioComercial();
        this.valorTotalProdutos = obj.getValorTotalProdutos();
        this.unidadeTributacao = obj.getUnidadeTributacao();
        this.quantidadeTributacao = obj.getQuantidadeTributacao();
        this.valorUnitarioTributacao = obj.getValorUnitarioTributacao();
        this.valorDesconto = obj.getValorDesconto();
        this.valorFrete = obj.getValorFrete();
        this.valorSeguro = obj.getValorSeguro();
        this.valorOutro = obj.getValorOutro();
        this.informacaoAdicional = obj.getInformacaoAdicional();
        this.validadeLote = obj.getValidadeLote();
        this.fabricacaoLote = obj.getFabricacaoLote();
        this.numeroLote = obj.getNumeroLote();
        this.cstIcms = obj.getCstIcms();
        this.modBc = obj.getModBc();
        this.origIcms = obj.getOrigIcms();
        this.aliqIcms = obj.getAliqIcms();
        this.bcIcms = obj.getBcIcms();
        this.valorIcms = obj.getValorIcms();
        this.aliqFcp = obj.getAliqFcp();
        this.valorFcp = obj.getValorFcp();
        this.modBcSt = obj.getModBcSt();
        this.motDesIcms = obj.getMotDesIcms();
        this.valorIcmsDesonerado = obj.getValorIcmsDesonerado();
        this.aliqFcpSt = obj.getAliqFcpSt();
        this.aliqIcmsSt = obj.getAliqIcmsSt();
        this.percentMargemIcmsSt = obj.getPercentMargemIcmsSt();
        this.percentRedBc = obj.getPercentRedBc();
        this.percentRedBcSt = obj.getPercentRedBcSt();
        this.valorIcmsSt = obj.getValorIcmsSt();
        this.valorFcpSt = obj.getValorFcpSt();
        this.bcIcmsSt = obj.getBcIcmsSt();
        this.bcFcp = obj.getBcFcp();
        this.bcFcpSt = obj.getBcFcpSt();
        this.aliqIcmsDiferido = obj.getAliqIcmsDiferido();
        this.valorIcmsDiferido = obj.getValorIcmsDiferido();
        this.valorIcmsOperacao = obj.getValorIcmsOperacao();
        this.aliqFcpStRetido = obj.getAliqFcpStRetido();
        this.aliqIcmsEfetivo = obj.getAliqIcmsEfetivo();
        this.percentRedBcEfetivo = obj.getPercentRedBcEfetivo();
        this.bcIcmsEfetivo = obj.getBcIcmsEfetivo();
        this.bcFcpStRetido = obj.getBcFcpStRetido();
        this.valorIcmsEfetivo = obj.getValorIcmsEfetivo();
        this.valorIcmsSubstituto = obj.getValorIcmsSubstituto();
        this.valorFcpStRetido = obj.getValorFcpStRetido();
        this.csoSn = obj.getCsoSn();
        this.pstIcms = obj.getPstIcms();
        this.aliqCredSn = obj.getAliqCredSn();
        this.valorCredIcmsSn = obj.getValorCredIcmsSn();
        this.bcIcmsStRetido = obj.getBcIcmsStRetido();
        this.bcIcmsStDestino = obj.getBcIcmsStDestino();
        this.valorIcmsStRetido = obj.getValorIcmsStRetido();
        this.valorIcmsStDestino = obj.getValorIcmsStDestino();
        this.percentBcOperacao = obj.getPercentBcOperacao();
        this.ufIcmsSt = obj.getUfIcmsSt();
        this.cstPis = obj.getCstPis();
        this.aliqPis = obj.getAliqPis();
        this.bcPis = obj.getBcPis();
        this.valorPis = obj.getValorPis();
        this.cstCofins = obj.getCstCofins();
        this.aliqCofins = obj.getAliqCofins();
        this.quantVendidaCofins = obj.getQuantVendidaCofins();
        this.valorAliqCofinsRs = obj.getValorAliqCofinsRs();
        this.bcCofins = obj.getBcCofins();
        this.valorCofins = obj.getValorCofins();
        this.enquadramentoIpi = obj.getEnquadramentoIpi();
        this.cnpjProdIpi = obj.getCnpjProdIpi();
        this.codigoSeloIpi = obj.getCodigoSeloIpi();
        this.quantidadeSeloIpi = obj.getQuantidadeSeloIpi();
        this.cstIpi = obj.getCstIpi();
        this.aliqIpi = obj.getAliqIpi();
        this.quantUniIpi = obj.getQuantUniIpi();
        this.bcIpi = obj.getBcIpi();
        this.valorIpi = obj.getValorIpi();
        this.valorUniIpi = obj.getValorUniIpi();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(String numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNcmSh() {
        return ncmSh;
    }

    public void setNcmSh(String ncmSh) {
        this.ncmSh = ncmSh;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnidadeComercial() {
        return unidadeComercial;
    }

    public void setUnidadeComercial(String unidadeComercial) {
        this.unidadeComercial = unidadeComercial;
    }

    public BigDecimal getQuantidadeComercial() {
        return quantidadeComercial;
    }

    public void setQuantidadeComercial(BigDecimal quantidadeComercial) {
        this.quantidadeComercial = quantidadeComercial;
    }

    public BigDecimal getValorUnitarioComercial() {
        return valorUnitarioComercial;
    }

    public void setValorUnitarioComercial(BigDecimal valorUnitarioComercial) {
        this.valorUnitarioComercial = valorUnitarioComercial;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public String getUnidadeTributacao() {
        return unidadeTributacao;
    }

    public void setUnidadeTributacao(String unidadeTributacao) {
        this.unidadeTributacao = unidadeTributacao;
    }

    public BigDecimal getQuantidadeTributacao() {
        return quantidadeTributacao;
    }

    public void setQuantidadeTributacao(BigDecimal quantidadeTributacao) {
        this.quantidadeTributacao = quantidadeTributacao;
    }

    public BigDecimal getValorUnitarioTributacao() {
        return valorUnitarioTributacao;
    }

    public void setValorUnitarioTributacao(BigDecimal valorUnitarioTributacao) {
        this.valorUnitarioTributacao = valorUnitarioTributacao;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
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

    public BigDecimal getValorOutro() {
        return valorOutro;
    }

    public void setValorOutro(BigDecimal valorOutro) {
        this.valorOutro = valorOutro;
    }

    public String getInformacaoAdicional() {
        return informacaoAdicional;
    }

    public void setInformacaoAdicional(String informacaoAdicional) {
        this.informacaoAdicional = informacaoAdicional;
    }

    public String getValidadeLote() {
        return validadeLote;
    }

    public void setValidadeLote(String validadeLote) {
        this.validadeLote = validadeLote;
    }

    public String getFabricacaoLote() {
        return fabricacaoLote;
    }

    public void setFabricacaoLote(String fabricacaoLote) {
        this.fabricacaoLote = fabricacaoLote;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getCstIcms() {
        return cstIcms;
    }

    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }

    public String getModBc() {
        return modBc;
    }

    public void setModBc(String modBc) {
        this.modBc = modBc;
    }

    public String getOrigIcms() {
        return origIcms;
    }

    public void setOrigIcms(String origIcms) {
        this.origIcms = origIcms;
    }

    public BigDecimal getAliqIcms() {
        return aliqIcms;
    }

    public void setAliqIcms(BigDecimal aliqIcms) {
        this.aliqIcms = aliqIcms;
    }

    public BigDecimal getBcIcms() {
        return bcIcms;
    }

    public void setBcIcms(BigDecimal bcIcms) {
        this.bcIcms = bcIcms;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public BigDecimal getAliqFcp() {
        return aliqFcp;
    }

    public void setAliqFcp(BigDecimal aliqFcp) {
        this.aliqFcp = aliqFcp;
    }

    public BigDecimal getValorFcp() {
        return valorFcp;
    }

    public void setValorFcp(BigDecimal valorFcp) {
        this.valorFcp = valorFcp;
    }

    public String getModBcSt() {
        return modBcSt;
    }

    public void setModBcSt(String modBcSt) {
        this.modBcSt = modBcSt;
    }

    public String getMotDesIcms() {
        return motDesIcms;
    }

    public void setMotDesIcms(String motDesIcms) {
        this.motDesIcms = motDesIcms;
    }

    public BigDecimal getValorIcmsDesonerado() {
        return valorIcmsDesonerado;
    }

    public void setValorIcmsDesonerado(BigDecimal valorIcmsDesonerado) {
        this.valorIcmsDesonerado = valorIcmsDesonerado;
    }

    public BigDecimal getAliqFcpSt() {
        return aliqFcpSt;
    }

    public void setAliqFcpSt(BigDecimal aliqFcpSt) {
        this.aliqFcpSt = aliqFcpSt;
    }

    public BigDecimal getAliqIcmsSt() {
        return aliqIcmsSt;
    }

    public void setAliqIcmsSt(BigDecimal aliqIcmsSt) {
        this.aliqIcmsSt = aliqIcmsSt;
    }

    public BigDecimal getPercentMargemIcmsSt() {
        return percentMargemIcmsSt;
    }

    public void setPercentMargemIcmsSt(BigDecimal percentMargemIcmsSt) {
        this.percentMargemIcmsSt = percentMargemIcmsSt;
    }

    public BigDecimal getPercentRedBc() {
        return percentRedBc;
    }

    public void setPercentRedBc(BigDecimal percentRedBc) {
        this.percentRedBc = percentRedBc;
    }

    public BigDecimal getPercentRedBcSt() {
        return percentRedBcSt;
    }

    public void setPercentRedBcSt(BigDecimal percentRedBcSt) {
        this.percentRedBcSt = percentRedBcSt;
    }

    public BigDecimal getValorIcmsSt() {
        return valorIcmsSt;
    }

    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }

    public BigDecimal getValorFcpSt() {
        return valorFcpSt;
    }

    public void setValorFcpSt(BigDecimal valorFcpSt) {
        this.valorFcpSt = valorFcpSt;
    }

    public BigDecimal getBcIcmsSt() {
        return bcIcmsSt;
    }

    public void setBcIcmsSt(BigDecimal bcIcmsSt) {
        this.bcIcmsSt = bcIcmsSt;
    }

    public BigDecimal getBcFcp() {
        return bcFcp;
    }

    public void setBcFcp(BigDecimal bcFcp) {
        this.bcFcp = bcFcp;
    }

    public BigDecimal getBcFcpSt() {
        return bcFcpSt;
    }

    public void setBcFcpSt(BigDecimal bcFcpSt) {
        this.bcFcpSt = bcFcpSt;
    }

    public BigDecimal getAliqIcmsDiferido() {
        return aliqIcmsDiferido;
    }

    public void setAliqIcmsDiferido(BigDecimal aliqIcmsDiferido) {
        this.aliqIcmsDiferido = aliqIcmsDiferido;
    }

    public BigDecimal getValorIcmsDiferido() {
        return valorIcmsDiferido;
    }

    public void setValorIcmsDiferido(BigDecimal valorIcmsDiferido) {
        this.valorIcmsDiferido = valorIcmsDiferido;
    }

    public BigDecimal getValorIcmsOperacao() {
        return valorIcmsOperacao;
    }

    public void setValorIcmsOperacao(BigDecimal valorIcmsOperacao) {
        this.valorIcmsOperacao = valorIcmsOperacao;
    }

    public BigDecimal getAliqFcpStRetido() {
        return aliqFcpStRetido;
    }

    public void setAliqFcpStRetido(BigDecimal aliqFcpStRetido) {
        this.aliqFcpStRetido = aliqFcpStRetido;
    }

    public BigDecimal getAliqIcmsEfetivo() {
        return aliqIcmsEfetivo;
    }

    public void setAliqIcmsEfetivo(BigDecimal aliqIcmsEfetivo) {
        this.aliqIcmsEfetivo = aliqIcmsEfetivo;
    }

    public BigDecimal getPercentRedBcEfetivo() {
        return percentRedBcEfetivo;
    }

    public void setPercentRedBcEfetivo(BigDecimal percentRedBcEfetivo) {
        this.percentRedBcEfetivo = percentRedBcEfetivo;
    }

    public BigDecimal getBcIcmsEfetivo() {
        return bcIcmsEfetivo;
    }

    public void setBcIcmsEfetivo(BigDecimal bcIcmsEfetivo) {
        this.bcIcmsEfetivo = bcIcmsEfetivo;
    }

    public BigDecimal getBcFcpStRetido() {
        return bcFcpStRetido;
    }

    public void setBcFcpStRetido(BigDecimal bcFcpStRetido) {
        this.bcFcpStRetido = bcFcpStRetido;
    }

    public BigDecimal getValorIcmsEfetivo() {
        return valorIcmsEfetivo;
    }

    public void setValorIcmsEfetivo(BigDecimal valorIcmsEfetivo) {
        this.valorIcmsEfetivo = valorIcmsEfetivo;
    }

    public BigDecimal getValorIcmsSubstituto() {
        return valorIcmsSubstituto;
    }

    public void setValorIcmsSubstituto(BigDecimal valorIcmsSubstituto) {
        this.valorIcmsSubstituto = valorIcmsSubstituto;
    }

    public BigDecimal getValorFcpStRetido() {
        return valorFcpStRetido;
    }

    public void setValorFcpStRetido(BigDecimal valorFcpStRetido) {
        this.valorFcpStRetido = valorFcpStRetido;
    }

    public String getCsoSn() {
        return csoSn;
    }

    public void setCsoSn(String csoSn) {
        this.csoSn = csoSn;
    }

    public BigDecimal getPstIcms() {
        return pstIcms;
    }

    public void setPstIcms(BigDecimal pstIcms) {
        this.pstIcms = pstIcms;
    }

    public BigDecimal getAliqCredSn() {
        return aliqCredSn;
    }

    public void setAliqCredSn(BigDecimal aliqCredSn) {
        this.aliqCredSn = aliqCredSn;
    }

    public BigDecimal getValorCredIcmsSn() {
        return valorCredIcmsSn;
    }

    public void setValorCredIcmsSn(BigDecimal valorCredIcmsSn) {
        this.valorCredIcmsSn = valorCredIcmsSn;
    }

    public BigDecimal getBcIcmsStRetido() {
        return bcIcmsStRetido;
    }

    public void setBcIcmsStRetido(BigDecimal bcIcmsStRetido) {
        this.bcIcmsStRetido = bcIcmsStRetido;
    }

    public BigDecimal getBcIcmsStDestino() {
        return bcIcmsStDestino;
    }

    public void setBcIcmsStDestino(BigDecimal bcIcmsStDestino) {
        this.bcIcmsStDestino = bcIcmsStDestino;
    }

    public BigDecimal getValorIcmsStRetido() {
        return valorIcmsStRetido;
    }

    public void setValorIcmsStRetido(BigDecimal valorIcmsStRetido) {
        this.valorIcmsStRetido = valorIcmsStRetido;
    }

    public BigDecimal getValorIcmsStDestino() {
        return valorIcmsStDestino;
    }

    public void setValorIcmsStDestino(BigDecimal valorIcmsStDestino) {
        this.valorIcmsStDestino = valorIcmsStDestino;
    }

    public BigDecimal getPercentBcOperacao() {
        return percentBcOperacao;
    }

    public void setPercentBcOperacao(BigDecimal percentBcOperacao) {
        this.percentBcOperacao = percentBcOperacao;
    }

    public String getUfIcmsSt() {
        return ufIcmsSt;
    }

    public void setUfIcmsSt(String ufIcmsSt) {
        this.ufIcmsSt = ufIcmsSt;
    }

    public String getCstPis() {
        return cstPis;
    }

    public void setCstPis(String cstPis) {
        this.cstPis = cstPis;
    }

    public BigDecimal getAliqPis() {
        return aliqPis;
    }

    public void setAliqPis(BigDecimal aliqPis) {
        this.aliqPis = aliqPis;
    }

    public BigDecimal getBcPis() {
        return bcPis;
    }

    public void setBcPis(BigDecimal bcPis) {
        this.bcPis = bcPis;
    }

    public BigDecimal getValorPis() {
        return valorPis;
    }

    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }

    public String getCstCofins() {
        return cstCofins;
    }

    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }

    public BigDecimal getAliqCofins() {
        return aliqCofins;
    }

    public void setAliqCofins(BigDecimal aliqCofins) {
        this.aliqCofins = aliqCofins;
    }

    public BigDecimal getQuantVendidaCofins() {
        return quantVendidaCofins;
    }

    public void setQuantVendidaCofins(BigDecimal quantVendidaCofins) {
        this.quantVendidaCofins = quantVendidaCofins;
    }

    public BigDecimal getValorAliqCofinsRs() {
        return valorAliqCofinsRs;
    }

    public void setValorAliqCofinsRs(BigDecimal valorAliqCofinsRs) {
        this.valorAliqCofinsRs = valorAliqCofinsRs;
    }

    public BigDecimal getBcCofins() {
        return bcCofins;
    }

    public void setBcCofins(BigDecimal bcCofins) {
        this.bcCofins = bcCofins;
    }

    public BigDecimal getValorCofins() {
        return valorCofins;
    }

    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }

    public String getEnquadramentoIpi() {
        return enquadramentoIpi;
    }

    public void setEnquadramentoIpi(String enquadramentoIpi) {
        this.enquadramentoIpi = enquadramentoIpi;
    }

    public String getCnpjProdIpi() {
        return cnpjProdIpi;
    }

    public void setCnpjProdIpi(String cnpjProdIpi) {
        this.cnpjProdIpi = cnpjProdIpi;
    }

    public String getCodigoSeloIpi() {
        return codigoSeloIpi;
    }

    public void setCodigoSeloIpi(String codigoSeloIpi) {
        this.codigoSeloIpi = codigoSeloIpi;
    }

    public BigDecimal getQuantidadeSeloIpi() {
        return quantidadeSeloIpi;
    }

    public void setQuantidadeSeloIpi(BigDecimal quantidadeSeloIpi) {
        this.quantidadeSeloIpi = quantidadeSeloIpi;
    }

    public String getCstIpi() {
        return cstIpi;
    }

    public void setCstIpi(String cstIpi) {
        this.cstIpi = cstIpi;
    }

    public BigDecimal getAliqIpi() {
        return aliqIpi;
    }

    public void setAliqIpi(BigDecimal aliqIpi) {
        this.aliqIpi = aliqIpi;
    }

    public BigDecimal getQuantUniIpi() {
        return quantUniIpi;
    }

    public void setQuantUniIpi(BigDecimal quantUniIpi) {
        this.quantUniIpi = quantUniIpi;
    }

    public BigDecimal getBcIpi() {
        return bcIpi;
    }

    public void setBcIpi(BigDecimal bcIpi) {
        this.bcIpi = bcIpi;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public BigDecimal getValorUniIpi() {
        return valorUniIpi;
    }

    public void setValorUniIpi(BigDecimal valorUniIpi) {
        this.valorUniIpi = valorUniIpi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscalItens that = (NotaFiscalItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
