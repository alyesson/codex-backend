package br.com.codex.v1.domain.dto;


import br.com.codex.v1.domain.contabilidade.NotaFiscalItem;
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

    /* ------------ GRUPO PROD (det) ------------ */
    private Integer numeroItem;
    private String codigoProduto;
    private String descricao;
    private String cean;
    private String ncm;
    private Integer cest;
    private String extipi;
    private String cfop;
    private String unidadeComercial;
    private BigDecimal quantidadeComercial;
    private BigDecimal valorUnitarioComercial;
    private BigDecimal valorBruto;
    private String codigoBarrasTributavel;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDesconto;
    private BigDecimal valorOutrasDespesas;
    private String pedidoCompra;

    // Tributos
    private Integer icmsOrigem;
    private Integer icmsSituacaoTributaria;
    private BigDecimal icmsBaseCalculo;
    private BigDecimal icmsAliquota;
    private BigDecimal icmsValor;
    private Integer ipiSituacaoTributaria;
    private BigDecimal ipiBaseCalculo;
    private BigDecimal ipiAliquota;
    private BigDecimal ipiValor;
    private Integer pisSituacaoTributaria;
    private BigDecimal pisBaseCalculo;
    private BigDecimal pisAliquota;
    private BigDecimal pisValor;
    private Integer cofinsSituacaoTributaria;
    private BigDecimal cofinsBaseCalculo;
    private BigDecimal cofinsAliquota;
    private BigDecimal cofinsValor;

    private String informacoesAdicionaisItem;

    public NotaFiscalItemDto() {
        super();
    }

    public NotaFiscalItemDto(NotaFiscalItem obj) {
        this.id = obj.getId();
        this.notaFiscalId = obj.getNotaFiscal() != null ? obj.getNotaFiscal().getId() : null;
        this.numeroItem = obj.getNumeroItem();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricao = obj.getDescricao();
        this.cean = obj.getCean();
        this.ncm = obj.getNcm();
        this.cest = obj.getCest();
        this.extipi = obj.getExtipi();
        this.cfop = obj.getCfop();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.quantidadeComercial = obj.getQuantidadeComercial();
        this.valorUnitarioComercial = obj.getValorUnitarioComercial();
        this.valorBruto = obj.getValorBruto();
        this.codigoBarrasTributavel = obj.getCodigoBarrasTributavel();
        this.valorFrete = obj.getValorFrete();
        this.valorSeguro = obj.getValorSeguro();
        this.valorDesconto = obj.getValorDesconto();
        this.valorOutrasDespesas = obj.getValorOutrasDespesas();
        this.pedidoCompra = obj.getPedidoCompra();
        this.icmsOrigem = obj.getIcmsOrigem();
        this.icmsSituacaoTributaria = obj.getIcmsSituacaoTributaria();
        this.icmsBaseCalculo = obj.getIcmsBaseCalculo();
        this.icmsAliquota = obj.getIcmsAliquota();
        this.icmsValor = obj.getIcmsValor();
        this.ipiSituacaoTributaria = obj.getIpiSituacaoTributaria();
        this.ipiBaseCalculo = obj.getIpiBaseCalculo();
        this.ipiAliquota = obj.getIpiAliquota();
        this.ipiValor = obj.getIpiValor();
        this.pisSituacaoTributaria = obj.getPisSituacaoTributaria();
        this.pisBaseCalculo = obj.getPisBaseCalculo();
        this.pisAliquota = obj.getPisAliquota();
        this.pisValor = obj.getPisValor();
        this.cofinsSituacaoTributaria = obj.getCofinsSituacaoTributaria();
        this.cofinsBaseCalculo = obj.getCofinsBaseCalculo();
        this.cofinsAliquota = obj.getCofinsAliquota();
        this.cofinsValor = obj.getCofinsValor();
        this.informacoesAdicionaisItem = obj.getInformacoesAdicionaisItem();
    }
}

