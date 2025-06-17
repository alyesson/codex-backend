package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.VendaItensDto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class VendaItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String cpf;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected BigDecimal valorProduto;
    protected BigDecimal valorTotal;

    public VendaItens() {
        super();
    }

    public VendaItens(Long id, String cpf, String codigoProduto, String descricaoProduto, Integer quantidade, BigDecimal valorProduto, BigDecimal valorTotal) {
        this.id = id;
        this.cpf = cpf;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.valorProduto = valorProduto;
        this.valorTotal = valorTotal;
    }

    public VendaItens(VendaItensDto objItens) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendaItens that = (VendaItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
