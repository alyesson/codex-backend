package br.com.codex.v1.domain.vendas;

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
public class VendaItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal quantidade = BigDecimal.ONE;

    @Column(nullable = false)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public VendaItens() {
        super();
    }

    public VendaItens(Long id, Venda venda, String codigo, String descricao, BigDecimal quantidade,
                      BigDecimal valorUnitario, BigDecimal valorTotal) {
        this.id = id;
        this.venda = venda;
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VendaItens that = (VendaItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
