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

    /* FK — chave estrangeira para NotaFiscal.id */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    private NotaFiscal notaFiscal;

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

    public NotaFiscalItem() {
        super();
    }

    public NotaFiscalItem(Long id, NotaFiscal notaFiscal, Integer numeroItem, String codigoProduto, String descricao, String cean, String ncm, Integer cest, String extipi, String cfop, String unidadeComercial, BigDecimal quantidadeComercial, BigDecimal valorUnitarioComercial, BigDecimal valorBruto, String codigoBarrasTributavel, BigDecimal valorFrete, BigDecimal valorSeguro, BigDecimal valorDesconto, BigDecimal valorOutrasDespesas, String pedidoCompra, Integer icmsOrigem, Integer icmsSituacaoTributaria, BigDecimal icmsBaseCalculo, BigDecimal icmsAliquota, BigDecimal icmsValor, Integer ipiSituacaoTributaria, BigDecimal ipiBaseCalculo, BigDecimal ipiAliquota, BigDecimal ipiValor, Integer pisSituacaoTributaria, BigDecimal pisBaseCalculo, BigDecimal pisAliquota, BigDecimal pisValor, Integer cofinsSituacaoTributaria, BigDecimal cofinsBaseCalculo, BigDecimal cofinsAliquota, BigDecimal cofinsValor, String informacoesAdicionaisItem) {
        this.id = id;
        this.notaFiscal = notaFiscal;
        this.numeroItem = numeroItem;
        this.codigoProduto = codigoProduto;
        this.descricao = descricao;
        this.cean = cean;
        this.ncm = ncm;
        this.cest = cest;
        this.extipi = extipi;
        this.cfop = cfop;
        this.unidadeComercial = unidadeComercial;
        this.quantidadeComercial = quantidadeComercial;
        this.valorUnitarioComercial = valorUnitarioComercial;
        this.valorBruto = valorBruto;
        this.codigoBarrasTributavel = codigoBarrasTributavel;
        this.valorFrete = valorFrete;
        this.valorSeguro = valorSeguro;
        this.valorDesconto = valorDesconto;
        this.valorOutrasDespesas = valorOutrasDespesas;
        this.pedidoCompra = pedidoCompra;
        this.icmsOrigem = icmsOrigem;
        this.icmsSituacaoTributaria = icmsSituacaoTributaria;
        this.icmsBaseCalculo = icmsBaseCalculo;
        this.icmsAliquota = icmsAliquota;
        this.icmsValor = icmsValor;
        this.ipiSituacaoTributaria = ipiSituacaoTributaria;
        this.ipiBaseCalculo = ipiBaseCalculo;
        this.ipiAliquota = ipiAliquota;
        this.ipiValor = ipiValor;
        this.pisSituacaoTributaria = pisSituacaoTributaria;
        this.pisBaseCalculo = pisBaseCalculo;
        this.pisAliquota = pisAliquota;
        this.pisValor = pisValor;
        this.cofinsSituacaoTributaria = cofinsSituacaoTributaria;
        this.cofinsBaseCalculo = cofinsBaseCalculo;
        this.cofinsAliquota = cofinsAliquota;
        this.cofinsValor = cofinsValor;
        this.informacoesAdicionaisItem = informacoesAdicionaisItem;
    }

    public NotaFiscalItem(NotaFiscalItemDto obj) {
        this.id = obj.getId();
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
        this.pedidoCompra =obj.getPedidoCompra();
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