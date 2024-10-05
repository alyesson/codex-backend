package br.com.codex.v1.domain.compras;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class SolicitacaoItensCompra implements Serializable {
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
    @ManyToOne
    @JoinColumn(name = "solicitacaoCompra_id")
    private SolicitacaoCompra solicitacaoCompra;

    public SolicitacaoItensCompra() {
        super();
    }

    public SolicitacaoItensCompra(Integer id, String codigoProduto, String descricaoProduto, Integer quantidade, String unidadeComercial, BigDecimal precoUnitario, SolicitacaoCompra solicitacaoCompra) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.precoUnitario = precoUnitario;
        this.solicitacaoCompra = solicitacaoCompra;
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

    public SolicitacaoCompra getSolicitacaoCompra() {
        return solicitacaoCompra;
    }

    public void setSolicitacaoCompra(SolicitacaoCompra solicitacaoCompra) {
        this.solicitacaoCompra = solicitacaoCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitacaoItensCompra that = (SolicitacaoItensCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
