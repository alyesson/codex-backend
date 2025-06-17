package br.com.codex.v1.domain.dto;


import br.com.codex.v1.domain.vendas.VendaItens;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class VendaItensDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String cpf;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected BigDecimal valorProduto;
    protected BigDecimal valorTotal;

    public VendaItensDto() {
        super();
    }

    public VendaItensDto(VendaItens objItens) {
        this.id = objItens.getId();
        this.cpf = objItens.getCpf();
        this.codigoProduto = objItens.getCodigoProduto();
        this.descricaoProduto = objItens.getDescricaoProduto();
        this.quantidade = objItens.getQuantidade();
        this.valorProduto = objItens.getValorProduto();
        this.valorTotal = objItens.getValorTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public BigDecimal getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(BigDecimal valorProduto) {
        this.valorProduto = valorProduto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}