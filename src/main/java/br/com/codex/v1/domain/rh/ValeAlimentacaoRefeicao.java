package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ValeAlimentacaoRefeicao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private CadastroColaboradores colaborador;

    @Column(nullable = false)
    private LocalDate dataInicio;
    private LocalDate dataFim;

    @Column(nullable = false, length = 20)
    private String tipoBeneficio;

    @Column(nullable = false, length = 20)
    private String formaUtilizacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorDiarioAlimentacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorDiarioRefeicao;

    @Column(nullable = false)
    private Integer diasUteis;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotalAlimentacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotalRefeicao;

    @Column(length = 50)
    private String numeroCartao;

    @Column(length = 100)
    private String empresaFornecedora;

    @Column(length = 50)
    private String bandeiraCartao; // Sodexo, Ticket, Alelo, VR, etc.
    private Boolean refeitorioInterno; // Se usa refeitório da empresa
    private Boolean restauranteConveniado; // Se usa restaurantes conveniados

    @Column(length = 500)
    private String observacoes;

    @Column(nullable = false)
    private Boolean ativo;

    private LocalDate dataVencimentoCartao;
    private LocalDate dataBloqueio;

    public ValeAlimentacaoRefeicao() {
        super();
        this.ativo = true;
        this.diasUteis = 22; // Padrão 22 dias úteis
        this.refeitorioInterno = false;
        this.restauranteConveniado = true;
    }

    public ValeAlimentacaoRefeicao(Long id, CadastroColaboradores colaborador, LocalDate dataInicio,
                                   LocalDate dataFim, String tipoBeneficio, String formaUtilizacao,
                                   BigDecimal valorDiarioAlimentacao, BigDecimal valorDiarioRefeicao,
                                   Integer diasUteis, BigDecimal valorTotalAlimentacao,
                                   BigDecimal valorTotalRefeicao, String numeroCartao,
                                   String empresaFornecedora, String bandeiraCartao,
                                   Boolean refeitorioInterno, Boolean restauranteConveniado,
                                   String observacoes, Boolean ativo, LocalDate dataVencimentoCartao,
                                   LocalDate dataBloqueio) {
        this.id = id;
        this.colaborador = colaborador;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.tipoBeneficio = tipoBeneficio;
        this.formaUtilizacao = formaUtilizacao;
        this.valorDiarioAlimentacao = valorDiarioAlimentacao;
        this.valorDiarioRefeicao = valorDiarioRefeicao;
        this.diasUteis = diasUteis;
        this.valorTotalAlimentacao = valorTotalAlimentacao;
        this.valorTotalRefeicao = valorTotalRefeicao;
        this.numeroCartao = numeroCartao;
        this.empresaFornecedora = empresaFornecedora;
        this.bandeiraCartao = bandeiraCartao;
        this.refeitorioInterno = refeitorioInterno;
        this.restauranteConveniado = restauranteConveniado;
        this.observacoes = observacoes;
        this.ativo = ativo;
        this.dataVencimentoCartao = dataVencimentoCartao;
        this.dataBloqueio = dataBloqueio;
    }

    public void calcularValoresTotais() {
        if (valorDiarioAlimentacao != null && diasUteis != null) {
            this.valorTotalAlimentacao = valorDiarioAlimentacao.multiply(BigDecimal.valueOf(diasUteis));
        }
        if (valorDiarioRefeicao != null && diasUteis != null) {
            this.valorTotalRefeicao = valorDiarioRefeicao.multiply(BigDecimal.valueOf(diasUteis));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ValeAlimentacaoRefeicao that = (ValeAlimentacaoRefeicao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
