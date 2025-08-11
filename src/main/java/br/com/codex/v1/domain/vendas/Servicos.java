package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.ServicosDto;
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

    @Column(length = 6)
    private Duration tempoMedioExecucao;

    @Column(nullable = false, length = 30)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private Situacao situacao;

    @Column(nullable = false, length = 8)
    private BigDecimal baseCalculo = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal aliquotaPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal issPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal issValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal pisPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal pisValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cofinsPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cofinsValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal csllPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cssValor = BigDecimal.ZERO;

    @Column(nullable = false, length = 8)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column
    private String garantia;

    @Column(length = 500)
    private String observacoes;

    public Servicos() {
        super();
    }

    public Servicos(Long id, String codigo, LocalDate dataEmissao, TipoCobranca tipoCobranca, Duration tempoMedioExecucao,
                    String formaPagamento, Situacao situacao, BigDecimal baseCalculo, BigDecimal aliquotaPercentual,
                    BigDecimal issPercentual, BigDecimal issValor, BigDecimal pisPercentual, BigDecimal pisValor,
                    BigDecimal cofinsPercentual, BigDecimal cofinsValor, BigDecimal csllPercentual, BigDecimal cssValor,
                    BigDecimal valorTotal, String garantia, String observacoes) {
        this.id = id;
        this.codigo = codigo;
        this.dataEmissao = dataEmissao;
        this.tipoCobranca = tipoCobranca;
        this.tempoMedioExecucao = tempoMedioExecucao;
        this.formaPagamento = formaPagamento;
        this.situacao = situacao;
        this.baseCalculo = baseCalculo;
        this.aliquotaPercentual = aliquotaPercentual;
        this.issPercentual = issPercentual;
        this.issValor = issValor;
        this.pisPercentual = pisPercentual;
        this.pisValor = pisValor;
        this.cofinsPercentual = cofinsPercentual;
        this.cofinsValor = cofinsValor;
        this.csllPercentual = csllPercentual;
        this.cssValor = cssValor;
        this.valorTotal = valorTotal;
        this.garantia = garantia;
        this.observacoes = observacoes;
    }

    public Servicos(ServicosDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.dataEmissao = obj.getDataEmissao();
        this.tipoCobranca = obj.getTipoCobranca();
        this.tempoMedioExecucao = obj.getTempoMedioExecucao();
        this.formaPagamento = obj.getFormaPagamento();
        this.situacao = obj.getSituacao();
        this.baseCalculo = obj.getBaseCalculo();
        this.aliquotaPercentual = obj.getAliquotaPercentual();
        this.issPercentual = obj.getIssPercentual();
        this.issValor = obj.getIssValor();
        this.pisPercentual = obj.getPisPercentual();
        this.pisValor = obj.getPisValor();
        this.cofinsPercentual = obj.getCofinsPercentual();
        this.cofinsValor = obj.getCofinsValor();
        this.csllPercentual = obj.getCsllPercentual();
        this.cssValor = obj.getCssValor();
        this.valorTotal = obj.getValorTotal();
        this.garantia = obj.getGarantia();
        this.observacoes = obj.getObservacoes();
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
