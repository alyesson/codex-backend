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
public class SolicitacaoItensCompra implements Serializable {
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
    @JoinColumn(name = "solicitacaoCompra_id")
    private SolicitacaoCompra solicitacaoCompra;

    public SolicitacaoItensCompra() {
        super();
    }

    public SolicitacaoItensCompra(Long id, String codigoProduto, String descricaoProduto, Integer quantidade, String unidadeComercial, BigDecimal precoUnitario, SolicitacaoCompra solicitacaoCompra) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.unidadeComercial = unidadeComercial;
        this.precoUnitario = precoUnitario;
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
