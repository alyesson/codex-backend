package br.com.codex.v1.domain.compras;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CotacaoItensCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;
    protected BigDecimal frete;
    protected BigDecimal desconto;
    protected BigDecimal precoTotal;
    protected Integer quantidadePorUnidade;
    protected Integer quantidadeTotal;
    @ManyToOne
    @JoinColumn(name = "cotacaoCompra_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected CotacaoCompra cotacaoCompra;

    public CotacaoItensCompra() {
        super();
    }

    public CotacaoItensCompra(Long id, String codigoProduto, String descricaoProduto, Integer quantidade,
                              String unidadeComercial, BigDecimal precoUnitario, BigDecimal frete,
                              BigDecimal desconto, BigDecimal precoTotal, CotacaoCompra cotacaoCompra, Integer quantidadePorUnidade,
                              Integer quantidadeTotal) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.precoUnitario = precoUnitario;
        this.frete = frete;
        this.desconto = desconto;
        this.precoTotal = precoTotal;
        this.cotacaoCompra = cotacaoCompra;
        this.quantidadePorUnidade = quantidadePorUnidade;
        this.quantidadeTotal = quantidadeTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CotacaoItensCompra that = (CotacaoItensCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
