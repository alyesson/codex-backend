package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolhaMensalCalculadaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String matriculaColaborador;
    private String nomeColaborador;
    private LocalDate dataAdmissao;
    private String departamentoColaborador;
    private BigDecimal salarioBase;
    private BigDecimal salarioHora;
    private String jornada;
    private BigDecimal horasMes;
    private int dependentesIrrf;
    @NotNull(message = "Informar as horas trabalhadas por semana")
    private BigDecimal horasSemana;
    private BigDecimal insalubridade;
    private BigDecimal periculosidade;
    private BigDecimal pensaoAlimenticia;
    private BigDecimal horasExtras50;
    private BigDecimal horasExtras70;
    private BigDecimal horasExtras100;
    private BigDecimal salarioFamilia;
    private BigDecimal comissao;
    private BigDecimal quebraCaixa;
    private BigDecimal gratificacao;
    private String cargoFuncionario;
    private String centroDeCusto;
    private String contaDigito;
    private BigDecimal percentualAdicionalNoturno;
    private LocalTime horaEntrada;
    private LocalTime horaSaida;
    private BigDecimal valorVendaMes;
    private BigDecimal valorValeTransporte;
    private BigDecimal valorValAlimentacao;
    private BigDecimal valorValeRefeicao;
    private BigDecimal valorPlanoMedico;
    private BigDecimal valorPlanoOdonto;
    private BigDecimal valeFarmacia;
    private BigDecimal emprestimoConsignado;
    private BigDecimal contribuiSindical;
    private BigDecimal ajudaCusto;
    private int faltasMes;
    private int faltasDsr;
    private int faltasFeriados;
    private BigDecimal abonoSalarial;
    private BigDecimal participacaoLucrosResultado;
    private BigDecimal faltasHorasMes;
    private BigDecimal seguroVida;
    private BigDecimal valeCreche;
    private BigDecimal reembolsoViagem;
    private LocalDate dataProcessamento;
    private BigDecimal totalVencimentos;
    private BigDecimal totalDescontos;
    private BigDecimal valorLiquido;
    private Situacao situacao;
    private List<FolhaMensalEventosCalculadaDto> eventos = new ArrayList<>();

    public FolhaMensalCalculadaDto() {
        super();
    }

    public FolhaMensalCalculadaDto(FolhaMensalCalculada obj) {
        this.id = obj.getId();
        this.matriculaColaborador = obj.getMatriculaColaborador();
        this.nomeColaborador = obj.getNomeColaborador();
        this.dataAdmissao = obj.getDataAdmissao();
        this.departamentoColaborador = obj.getDepartamentoColaborador();
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
}
