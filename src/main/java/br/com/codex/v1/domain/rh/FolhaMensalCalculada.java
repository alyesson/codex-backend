package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.FolhaMensalCalculadaDto;
import br.com.codex.v1.domain.dto.FolhaMensalDto;
import br.com.codex.v1.domain.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FolhaMensalCalculada implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String matriculaColaborador;
    private String nomeColaborador;
    private LocalDate dataAdmissao;
    private String departColaborador;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioHora;

    private String jornada;

    @Column(precision = 10, scale = 2)
    private BigDecimal horasMes;

    private int dependentesIrrf;

    @Column(precision = 10, scale = 2)
    private BigDecimal horasSemana;

    @Column(precision = 10, scale = 2)
    private BigDecimal insalubridade;

    @Column(precision = 10, scale = 2)
    private BigDecimal periculosidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal pensaoAlimenticia;

    @Column(precision = 10, scale = 2)
    private BigDecimal horasExtras50;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioFamilia;

    @Column(precision = 10, scale = 2)
    private BigDecimal comissao;

    @Column(precision = 10, scale = 2)
    private BigDecimal quebraCaixa;

    @Column(precision = 10, scale = 2)
    private BigDecimal gratificacao;

    private String cargoFuncionario;
    private String centroDeCusto;
    private String contaDigito;

    @Column(precision = 10, scale = 2)
    private BigDecimal horasExtras70;

    @Column(precision = 10, scale = 2)
    private BigDecimal horasExtras100;

    @Column(precision = 10, scale = 2)
    private BigDecimal percentualAdicionalNoturno;

    private LocalTime horaEntrada;
    private LocalTime horaSaida;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorVendaMes;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorValeTransporte;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorValAlimentacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorValeRefeicao;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorPlanoMedico;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorPlanoOdonto;

    @Column(precision = 10, scale = 2)
    private BigDecimal valeFarmacia;

    @Column(precision = 10, scale = 2)
    private BigDecimal emprestimoConsignado;

    @Column(precision = 10, scale = 2)
    private BigDecimal contribuiSindical;

    @Column(precision = 10, scale = 2)
    private BigDecimal ajudaCusto;

    private int faltasMes; //quantidade de faltas tidas no mês
    private int faltasDsr; //quantidade de faltas tidas do dsr no mês
    private int faltasFeriados; //quantidade de faltas de feriados tidas no mês

    @Column(precision = 10, scale = 2)
    private BigDecimal abonoSalarial;

    @Column(precision = 10, scale = 2)
    private BigDecimal participacaoLucrosResultado;

    @Column(precision = 10, scale = 2)
    private BigDecimal faltasHorasMes; //quantidade de faltas em horas tidas no mês

    @Column(precision = 10, scale = 2)
    private BigDecimal seguroVida;

    @Column(precision = 10, scale = 2)
    private BigDecimal valeCreche;

    @Column(precision = 10, scale = 2)
    private BigDecimal reembolsoViagem;

    private LocalDate dataProcessamento;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalVencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDescontos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorLiquido;

    private Situacao situacao;

    @JsonIgnore
    @OneToMany(mappedBy = "folhaMensalCalculada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolhaMensalEventosCalculada> eventos = new ArrayList<>();

    public FolhaMensalCalculada() {
        super();
    }

    public FolhaMensalCalculada(Long id, String matriculaColaborador, String nomeColaborador, LocalDate dataAdmissao,
                                String departColaborador, BigDecimal salarioBase, BigDecimal salarioHora, String jornada,
                                BigDecimal horasMes, int dependentesIrrf, BigDecimal horasSemana, BigDecimal insalubridade,
                                BigDecimal periculosidade, BigDecimal pensaoAlimenticia, BigDecimal horasExtras50, BigDecimal salarioFamilia,
                                BigDecimal comissao, BigDecimal quebraCaixa, BigDecimal gratificacao, String cargoFuncionario,
                                String centroDeCusto, String contaDigito, BigDecimal horasExtras70, BigDecimal horasExtras100,
                                BigDecimal percentualAdicionalNoturno, LocalTime horaEntrada, LocalTime horaSaida, BigDecimal valorVendaMes,
                                BigDecimal valorValeTransporte, BigDecimal valorValAlimentacao, BigDecimal valorValeRefeicao,
                                BigDecimal valorPlanoMedico, BigDecimal valorPlanoOdonto, BigDecimal valeFarmacia,
                                BigDecimal emprestimoConsignado, BigDecimal contribuiSindical, BigDecimal ajudaCusto,
                                int faltasMes, int faltasDsr, int faltasFeriados, BigDecimal abonoSalarial,
                                BigDecimal participacaoLucrosResultado, BigDecimal faltasHorasMes, BigDecimal seguroVida,
                                BigDecimal valeCreche, BigDecimal reembolsoViagem, LocalDate dataProcessamento,
                                BigDecimal totalVencimentos, BigDecimal totalDescontos, BigDecimal valorLiquido, Situacao situacao) {
        this.id = id;
        this.matriculaColaborador = matriculaColaborador;
        this.nomeColaborador = nomeColaborador;
        this.dataAdmissao = dataAdmissao;
        this.departColaborador = departColaborador;
        this.salarioBase = salarioBase;
        this.salarioHora = salarioHora;
        this.jornada = jornada;
        this.horasMes = horasMes;
        this.dependentesIrrf = dependentesIrrf;
        this.horasSemana = horasSemana;
        this.insalubridade = insalubridade;
        this.periculosidade = periculosidade;
        this.pensaoAlimenticia = pensaoAlimenticia;
        this.horasExtras50 = horasExtras50;
        this.salarioFamilia = salarioFamilia;
        this.comissao = comissao;
        this.quebraCaixa = quebraCaixa;
        this.gratificacao = gratificacao;
        this.cargoFuncionario = cargoFuncionario;
        this.centroDeCusto = centroDeCusto;
        this.contaDigito = contaDigito;
        this.horasExtras70 = horasExtras70;
        this.horasExtras100 = horasExtras100;
        this.percentualAdicionalNoturno = percentualAdicionalNoturno;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.valorVendaMes = valorVendaMes;
        this.valorValeTransporte = valorValeTransporte;
        this.valorValAlimentacao = valorValAlimentacao;
        this.valorValeRefeicao = valorValeRefeicao;
        this.valorPlanoMedico = valorPlanoMedico;
        this.valorPlanoOdonto = valorPlanoOdonto;
        this.valeFarmacia = valeFarmacia;
        this.emprestimoConsignado = emprestimoConsignado;
        this.contribuiSindical = contribuiSindical;
        this.ajudaCusto = ajudaCusto;
        this.faltasMes = faltasMes;
        this.faltasDsr = faltasDsr;
        this.faltasFeriados = faltasFeriados;
        this.abonoSalarial = abonoSalarial;
        this.participacaoLucrosResultado = participacaoLucrosResultado;
        this.faltasHorasMes = faltasHorasMes;
        this.seguroVida = seguroVida;
        this.valeCreche = valeCreche;
        this.reembolsoViagem = reembolsoViagem;
        this.dataProcessamento = dataProcessamento;
        this.totalVencimentos = totalVencimentos;
        this.totalDescontos = totalDescontos;
        this.valorLiquido = valorLiquido;
        this.situacao = situacao;
    }

    public FolhaMensalCalculada(FolhaMensalCalculadaDto obj) {
        this.id = obj.getId();
        this.matriculaColaborador = obj.getMatriculaColaborador();
        this.nomeColaborador = obj.getNomeColaborador();
        this.dataAdmissao = obj.getDataAdmissao();
        this.departColaborador = obj.getDepartColaborador();
        this.salarioBase = obj.getSalarioBase();
        this.salarioHora = obj.getSalarioHora();
        this.jornada = obj.getJornada();
        this.horasMes = obj.getHorasMes();
        this.dependentesIrrf = obj.getDependentesIrrf();
        this.horasSemana = obj.getHorasSemana();
        this.insalubridade = obj.getInsalubridade();
        this.periculosidade = obj.getPericulosidade();
        this.pensaoAlimenticia = obj.getPensaoAlimenticia();
        this.horasExtras50 = obj.getHorasExtras50();
        this.salarioFamilia = obj.getSalarioFamilia();
        this.comissao = obj.getComissao();
        this.quebraCaixa = obj.getQuebraCaixa();
        this.gratificacao = obj.getGratificacao();
        this.cargoFuncionario = obj.getCargoFuncionario();
        this.centroDeCusto = obj.getCentroDeCusto();
        this.contaDigito = obj.getContaDigito();
        this.horasExtras70 = obj.getHorasExtras70();
        this.horasExtras100 = obj.getHorasExtras100();
        this.percentualAdicionalNoturno = obj.getPercentualAdicionalNoturno();
        this.horaEntrada = obj.getHoraEntrada();
        this.horaSaida = obj.getHoraSaida();
        this.valorVendaMes = obj.getValorVendaMes();
        this.valorValeTransporte = obj.getValorValeTransporte();
        this.valorValAlimentacao = obj.getValorValAlimentacao();
        this.valorValeRefeicao = obj.getValorValeRefeicao();
        this.valorPlanoMedico = obj.getValorPlanoMedico();
        this.valorPlanoOdonto = obj.getValorPlanoOdonto();
        this.valeFarmacia = obj.getValeFarmacia();
        this.emprestimoConsignado = obj.getEmprestimoConsignado();
        this.contribuiSindical = obj.getContribuiSindical();
        this.ajudaCusto = obj.getAjudaCusto();
        this.faltasMes = obj.getFaltasMes();
        this.faltasDsr = obj.getFaltasDsr();
        this.faltasFeriados = obj.getFaltasFeriados();
        this.abonoSalarial = obj.getAbonoSalarial();
        this.participacaoLucrosResultado = obj.getParticipacaoLucrosResultado();
        this.faltasHorasMes = obj.getFaltasHorasMes();
        this.seguroVida = obj.getSeguroVida();
        this.valeCreche = obj.getValeCreche();
        this.reembolsoViagem = obj.getReembolsoViagem();
        this.dataProcessamento = obj.getDataProcessamento();
        this.totalVencimentos = obj.getTotalVencimentos();
        this.totalDescontos = obj.getTotalDescontos();
        this.valorLiquido = obj.getValorLiquido();
        this.situacao = obj.getSituacao();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaMensalCalculada that = (FolhaMensalCalculada) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
