package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.PedidoItensCompra;

import java.io.Serializable;
import java.math.BigDecimal;

public class PedidoItensCompraDto implements Serializable {
    private static final long serialVersionUID = 1L;

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
    protected PedidoCompraDto pedidoCompra;

    public PedidoItensCompraDto() {
        super();
    }

    public PedidoItensCompraDto(PedidoItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.precoUnitario = obj.getPrecoUnitario();
        this.ipi = obj.getIpi();
        this.pis = obj.getPis();
        this.icms = obj.getIcms();
        this.cofins = obj.getCofins();
        this.desconto = obj.getDesconto();
        this.precoTotal = obj.getPrecoTotal();
        this.pedidoCompra = new PedidoCompraDto(obj.getPedidoCompra());
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

    public PedidoCompraDto getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompraDto pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }
}
