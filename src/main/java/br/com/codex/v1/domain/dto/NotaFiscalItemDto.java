package br.com.codex.v1.domain.dto;


import br.com.codex.v1.domain.fiscal.NotaFiscalItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class NotaFiscalItemDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long notaFiscalId;
    private String numeroNotaFiscal;
    private String nomeProduto;
    private String codigoProduto;
    private String cean;
    private String ncmSh;
    private String cest;
    private String cfop;
    private String item;
    private String extipi;
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
    private String pedidoCompra;

    public NotaFiscalItemDto() {
        super();
    }

    public NotaFiscalItemDto(NotaFiscalItem obj) {
        this.id = obj.getId();
        this.numeroNotaFiscal = obj.getNumeroNotaFiscal();
        this.nomeProduto = obj.getNomeProduto();
        this.codigoProduto = obj.getCodigoProduto();
        this.cean = obj.getCean();
        this.ncmSh = obj.getNcmSh();
        this.cest = obj.getCest();
        this.cfop = obj.getCfop();
        this.item = obj.getItem();
        this.extipi = obj.getExtipi();
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
        this.pedidoCompra = obj.getPedidoCompra();
    }

    public void setNcmSh(String ncmSh) {
        this.ncmSh = ncmSh.replace(".", "");
    }

    public void setCest(String cest) {
        this.cest = cest.replace(".", "");
    }
}

