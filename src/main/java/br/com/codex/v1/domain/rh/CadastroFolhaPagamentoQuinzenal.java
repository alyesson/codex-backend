package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.cadastros.Departamento;
import br.com.codex.v1.domain.financeiro.CentroCusto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CadastroFolhaPagamentoQuinzenal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "colaborador_id", nullable = false)
    private CadastroColaboradores colaborador;

    @OneToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    private String mesReferencia;
    private LocalDate admissao;
    private String codigoCbo;
    private String descricaoCbo;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioHora;

    private String jornada;
    private String nomeBanco;
    private String agencia;
    private String numeroConta;

    @OneToOne
    @JoinColumn(name = "centro_custo_id", nullable = false)
    private CentroCusto centroCusto;

    private String tipoSalario;
    private BigDecimal transporteDia;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalVencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDescontos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorLiquido;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseCalculoIrrf;
}
