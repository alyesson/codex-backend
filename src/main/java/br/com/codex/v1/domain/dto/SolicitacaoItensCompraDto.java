package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class SolicitacaoItensCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected SolicitacaoCompraDto solicitacaoCompra;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;

    public SolicitacaoItensCompraDto() {
        super();
    }

    public SolicitacaoItensCompraDto(SolicitacaoItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.solicitacaoCompra = new SolicitacaoCompraDto(obj.getSolicitacaoCompra());
        this.unidadeComercial = obj.getUnidadeComercial();
        this.precoUnitario = obj.getPrecoUnitario();
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

    public SolicitacaoCompraDto getSolicitacaoCompra() {
        return solicitacaoCompra;
    }

    public void setSolicitacaoCompra(SolicitacaoCompraDto solicitacaoCompra) {
        this.solicitacaoCompra = solicitacaoCompra;
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
        this.precoUnitario = new BigDecimal("0.00");//precoUnitario;
    }
}
