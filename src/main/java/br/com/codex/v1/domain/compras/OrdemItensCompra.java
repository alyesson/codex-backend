package br.com.codex.v1.domain.compras;

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
public class OrdemItensCompra implements Serializable {
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
    @ManyToOne
    @JoinColumn(name = "ordemCompra_id")
    private OrdemCompra ordemCompra;

    public OrdemItensCompra() {
        super();
    }

    public OrdemItensCompra(Long id, String codigoProduto, String descricaoProduto, Integer quantidade, String unidadeComercial, BigDecimal precoUnitario, OrdemCompra ordemCompra) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.precoUnitario = precoUnitario;
        this.ordemCompra = ordemCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemItensCompra that = (OrdemItensCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
