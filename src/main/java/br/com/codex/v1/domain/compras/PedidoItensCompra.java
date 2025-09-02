package br.com.codex.v1.domain.compras;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Getter
@Setter
public class PedidoItensCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected String unidadeComercial;
    protected Integer quantidadePorUnidade;
    protected Integer quantidadeTotal;
    protected BigDecimal precoUnitario;
    protected BigDecimal ipi;
    protected BigDecimal pis;
    protected BigDecimal icms;
    protected BigDecimal cofins;
    protected BigDecimal desconto;
    protected BigDecimal frete;
    protected BigDecimal precoTotal;
    @ManyToOne
    @JoinColumn(name = "pedidoCompra_id", nullable = false)
    protected PedidoCompra pedidoCompra;

    public PedidoItensCompra() {
        super();
    }

    public PedidoItensCompra(Long id, String codigoProduto, String descricaoProduto, Integer quantidade,
                             String unidadeComercial, Integer quantidadePorUnidade, Integer quantidadeTotal,
                             BigDecimal precoUnitario, BigDecimal ipi, BigDecimal pis, BigDecimal icms, BigDecimal cofins,
                             BigDecimal desconto, BigDecimal frete, BigDecimal precoTotal, PedidoCompra pedidoCompra) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.quantidadePorUnidade = quantidadePorUnidade;
        this.quantidadeTotal = quantidadeTotal;
        this.precoUnitario = precoUnitario;
        this.ipi = ipi;
        this.pis = pis;
        this.icms = icms;
        this.cofins = cofins;
        this.desconto = desconto;
        this.frete = frete;
        this.precoTotal = precoTotal;
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
