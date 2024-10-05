package br.com.codex.v1.domain.compras;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class PedidoItensCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;
    protected BigDecimal ipi;
    protected BigDecimal pis;
    protected BigDecimal icms;
    protected BigDecimal cofins;
    protected BigDecimal desconto;
    protected BigDecimal precoTotal;
    @ManyToOne
    @JoinColumn(name = "pedidoCompra_id")
    protected PedidoCompra pedidoCompra;

    public PedidoItensCompra() {
        super();
    }

    public PedidoItensCompra(Integer id, String codigoProduto, String descricaoProduto, Integer quantidade, String unidadeComercial, BigDecimal precoUnitario, BigDecimal ipi, BigDecimal pis, BigDecimal icms, BigDecimal cofins, BigDecimal desconto, BigDecimal precoTotal, PedidoCompra pedidoCompra) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.precoUnitario = precoUnitario;
        this.ipi = ipi;
        this.pis = pis;
        this.icms = icms;
        this.cofins = cofins;
        this.desconto = desconto;
        this.precoTotal = precoTotal;
        this.pedidoCompra = pedidoCompra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidadeComercial() {
        return unidadeComercial;
    }

    public void setUnidadeComercial(String unidadeComercial) {
        this.unidadeComercial = unidadeComercial;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getIpi() {
        return ipi;
    }

    public void setIpi(BigDecimal ipi) {
        this.ipi = ipi;
    }

    public BigDecimal getPis() {
        return pis;
    }

    public void setPis(BigDecimal pis) {
        this.pis = pis;
    }

    public BigDecimal getIcms() {
        return icms;
    }

    public void setIcms(BigDecimal icms) {
        this.icms = icms;
    }

    public BigDecimal getCofins() {
        return cofins;
    }

    public void setCofins(BigDecimal cofins) {
        this.cofins = cofins;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public PedidoCompra getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedidoItensCompra that = (PedidoItensCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
