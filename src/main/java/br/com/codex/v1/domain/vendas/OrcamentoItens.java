package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import br.com.codex.v1.domain.dto.OrcamentoItensDto;
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
public class OrcamentoItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Column(nullable = false, length = 6)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal quantidade = BigDecimal.ONE;

    @Column(nullable = false)
    private BigDecimal valorUnitario = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal desconto = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public OrcamentoItens() {
        super();
    }

    public OrcamentoItens(Long id, Orcamento orcamento, String codigo, String descricao, BigDecimal quantidade,
                          BigDecimal valorUnitario, BigDecimal desconto, BigDecimal valorTotal) {
        this.id = id;
        this.orcamento = orcamento;
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.desconto = desconto;
        this.valorTotal = valorTotal;
    }

    public OrcamentoItens(OrcamentoItensDto obj) {
        this.id = obj.getId();
        this.orcamento = obj.getOrcamento();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.quantidade = obj.getQuantidade();
        this.valorUnitario = obj.getValorUnitario();
        this.desconto = obj.getDesconto();
        this.valorTotal = obj.getValorTotal();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrcamentoItens that = (OrcamentoItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
