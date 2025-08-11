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
public class ServicosItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "servicos_id", nullable = false)
    private Servicos servicos;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal quantidade = BigDecimal.ONE;

    @Column(nullable = false)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public ServicosItens() {
        super();
    }

    public ServicosItens(Long id, Servicos servicos, String codigo, String descricao, BigDecimal quantidade, BigDecimal valorUnitario, BigDecimal valorTotal) {
        this.id = id;
        this.servicos = servicos;
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServicosItens that = (ServicosItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
