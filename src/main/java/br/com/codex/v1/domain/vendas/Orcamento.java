package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.enums.Situacao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Orcamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String codigo;

    @Column(nullable = false)
    private String consumidor;

    @Column(nullable = false)
    private String documentoConsumidor;

    @Column(nullable = false, length = 11)
    private LocalDate dataEmissao;

    @Column(nullable = false, length = 11)
    private LocalDate dataValidade;

    @Column(nullable = false, length = 30)
    private String vendedor;

    @Column(nullable = false, length = 30)
    private String tipoOrcamento;

    @Column(nullable = false, length = 30)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private Situacao situacao;

    @Column(nullable = false, length = 30)
    private BigDecimal valorFrete = BigDecimal.ZERO;

    @Column(nullable = false, length = 6)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal descontoTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal valorFinal = BigDecimal.ZERO;

    @Column(length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoItens> itens;

    public Orcamento() {
        super();
    }

    public Orcamento(Long id, String codigo, String consumidor, String documentoConsumidor, LocalDate dataEmissao, LocalDate dataValidade, String vendedor,
                     String tipoOrcamento, String formaPagamento, Situacao situacao, BigDecimal valorFrete,
                     BigDecimal valorTotal, BigDecimal descontoTotal, BigDecimal valorFinal,
                     String observacoes, List<OrcamentoItens> itens) {
        this.id = id;
        this.codigo = codigo;
        this.consumidor = consumidor;
        this.documentoConsumidor = documentoConsumidor;
        this.dataEmissao = dataEmissao;
        this.dataValidade = dataValidade;
        this.vendedor = vendedor;
        this.tipoOrcamento = tipoOrcamento;
        this.formaPagamento = formaPagamento;
        this.situacao = situacao;
        this.valorFrete = valorFrete;
        this.valorTotal = valorTotal;
        this.descontoTotal = descontoTotal;
        this.valorFinal = valorFinal;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Orcamento orcamento = (Orcamento) o;
        return Objects.equals(id, orcamento.id) && Objects.equals(codigo, orcamento.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
