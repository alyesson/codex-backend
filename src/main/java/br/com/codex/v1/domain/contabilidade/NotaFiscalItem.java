package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class NotaFiscalItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    private NotaFiscal notaFiscal;

    /* ------------ GRUPO PROD (det) ------------ */
    private String numeroNotaFiscal;
    private String nomeProduto;
    private String codigoProduto;
    private String cEAN;
    private String cest;
    private String ncmSh;
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

    public NotaFiscalItem() {
        super();
    }

    public NotaFiscalItem(Long id, String numeroNotaFiscal, String nomeProduto, String codigoProduto, String cEAN,String ncmSh, String cest, String cfop, String item, String extipi,String unidadeComercial, BigDecimal quantidadeComercial, BigDecimal valorUnitarioComercial, BigDecimal valorTotalProdutos, String unidadeTributacao, BigDecimal quantidadeTributacao, BigDecimal valorUnitarioTributacao, BigDecimal valorDesconto, BigDecimal valorFrete, BigDecimal valorSeguro, BigDecimal valorOutro, String informacaoAdicional, String validadeLote, String fabricacaoLote, String numeroLote, String cstIcms, String modBc, String origIcms, BigDecimal aliqIcms, BigDecimal bcIcms, BigDecimal valorIcms, BigDecimal aliqFcp, BigDecimal valorFcp, String modBcSt, String motDesIcms, BigDecimal valorIcmsDesonerado, BigDecimal aliqFcpSt, BigDecimal aliqIcmsSt, BigDecimal percentMargemIcmsSt, BigDecimal percentRedBc, BigDecimal percentRedBcSt, BigDecimal valorIcmsSt, BigDecimal valorFcpSt, BigDecimal bcIcmsSt, BigDecimal bcFcp, BigDecimal bcFcpSt, BigDecimal aliqIcmsDiferido, BigDecimal valorIcmsDiferido, BigDecimal valorIcmsOperacao, BigDecimal aliqFcpStRetido, BigDecimal aliqIcmsEfetivo, BigDecimal percentRedBcEfetivo, BigDecimal bcIcmsEfetivo, BigDecimal bcFcpStRetido, BigDecimal valorIcmsEfetivo, BigDecimal valorIcmsSubstituto, BigDecimal valorFcpStRetido, String csoSn, BigDecimal pstIcms, BigDecimal aliqCredSn, BigDecimal valorCredIcmsSn, BigDecimal bcIcmsStRetido, BigDecimal bcIcmsStDestino, BigDecimal valorIcmsStRetido, BigDecimal valorIcmsStDestino, BigDecimal percentBcOperacao, String ufIcmsSt, String cstPis, BigDecimal aliqPis, BigDecimal bcPis, BigDecimal valorPis, String cstCofins, BigDecimal aliqCofins, BigDecimal quantVendidaCofins, BigDecimal valorAliqCofinsRs, BigDecimal bcCofins, BigDecimal valorCofins, String enquadramentoIpi, String cnpjProdIpi, String codigoSeloIpi, BigDecimal quantidadeSeloIpi, String cstIpi, BigDecimal aliqIpi, BigDecimal quantUniIpi, BigDecimal bcIpi, BigDecimal valorIpi, BigDecimal valorUniIpi, String pedidoCompra) {
        this.id = id;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.nomeProduto = nomeProduto;
        this.codigoProduto = codigoProduto;
        this.cEAN = cEAN;
        this.ncmSh = ncmSh;
        this.cest = cest;
        this.cfop = cfop;
        this.item = item;
        this.extipi = extipi;
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
        this.pedidoCompra = pedidoCompra;
    }

    public NotaFiscalItem(NotaFiscalItemDto obj) {
        this.id = obj.getId();
        this.numeroNotaFiscal = obj.getNumeroNotaFiscal();
        this.nomeProduto = obj.getNomeProduto();
        this.codigoProduto = obj.getCodigoProduto();
        this.cEAN = obj.getCEAN();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscalItem that = (NotaFiscalItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}