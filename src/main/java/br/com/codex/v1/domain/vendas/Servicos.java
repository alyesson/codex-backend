package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.enums.TipoCobranca;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Servicos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String codigo;

    @Column(nullable = false, length = 11)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private TipoCobranca tipoCobranca;

    @Column
    private Duration tempoMedioExecucao;

    @Column(nullable = false, length = 30)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private Situacao situacao;

    @Column(nullable = false, length = 6)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column
    private String garantia;

    @Column(length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "servicos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicosItens> itens;

    public Servicos() {
        super();
    }

    public Servicos(Long id, String codigo, LocalDate dataEmissao, TipoCobranca tipoCobranca, Duration tempoMedioExecucao,
                    String formaPagamento, Situacao situacao, BigDecimal valorTotal, String garantia, String observacoes, List<ServicosItens> itens) {
        this.id = id;
        this.codigo = codigo;
        this.dataEmissao = dataEmissao;
        this.tipoCobranca = tipoCobranca;
        this.tempoMedioExecucao = tempoMedioExecucao;
        this.formaPagamento = formaPagamento;
        this.situacao = situacao;
        this.valorTotal = valorTotal;
        this.garantia = garantia;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servicos servicos = (Servicos) o;
        return Objects.equals(id, servicos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
