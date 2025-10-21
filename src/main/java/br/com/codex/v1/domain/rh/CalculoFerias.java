package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CalculoFeriasDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class CalculoFerias implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroMatricula;
    private String nomeColaborador;
    private String codigoCbo;
    private String centroDeCusto;
    private LocalDate dataDeAdmissao;
    private String mesAnoReferencia;
    private String mesAnoReferenciaFolha;
    private String tipoDaFolha;
    private int numeroDependentes;
    private LocalDate dataAvisoFerias;
    private LocalDate dataReciboFerias;
    private LocalDate dataPagamento;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorMediaHoras;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBruto;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioHora;

    private String abonoPecuniario; //Sim ou não
    private String adiantar13;
    private LocalDate inicioAquisitivoFerias;
    private LocalDate terminoAquisitivoFerias;
    private int totalFaltas;
    private LocalDate dataInicioFerias;
    private LocalDate terminoFerias;
    private int totalDiasFerias;
    private String inicioAbono;
    private String terminoAbono;
    private int totalDiasAbono;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorDiasFerias;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalLiquidoReceber;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalBrutReceber;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDeDescontos;

    private String nomeEmpresa;
    private String cnpjEmpresa;

    @JsonIgnore
    @OneToMany(mappedBy = "calculoFerias", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalculoFeriasEventos> eventos = new ArrayList<>();

    public CalculoFerias() {
        super();
    }

    public CalculoFerias(Long id, String numeroMatricula, String nomeColaborador, String codigoCbo, String centroDeCusto,
                         LocalDate dataDeAdmissao, String mesAnoReferencia, String mesAnoReferenciaFolha, String tipoDaFolha,
                         int numeroDependentes, LocalDate dataAvisoFerias, LocalDate dataReciboFerias, LocalDate dataPagamento,
                         BigDecimal valorMediaHoras, BigDecimal salarioBruto, BigDecimal salarioHora, String abonoPecuniario,
                         String adiantar13, LocalDate inicioAquisitivoFerias, LocalDate terminoAquisitivoFerias,
                         int totalFaltas, LocalDate dataInicioFerias, LocalDate terminoFerias, int totalDiasFerias,
                         String inicioAbono, String terminoAbono, int totalDiasAbono, BigDecimal valorDiasFerias,
                         BigDecimal totalLiquidoReceber, BigDecimal totalBrutReceber, BigDecimal totalDeDescontos,
                         String nomeEmpresa, String cnpjEmpresa) {
        this.id = id;
        this.numeroMatricula = numeroMatricula;
        this.nomeColaborador = nomeColaborador;
        this.codigoCbo = codigoCbo;
        this.centroDeCusto = centroDeCusto;
        this.dataDeAdmissao = dataDeAdmissao;
        this.mesAnoReferencia = mesAnoReferencia;
        this.mesAnoReferenciaFolha = mesAnoReferenciaFolha;
        this.tipoDaFolha = tipoDaFolha;
        this.numeroDependentes = numeroDependentes;
        this.dataAvisoFerias = dataAvisoFerias;
        this.dataReciboFerias = dataReciboFerias;
        this.dataPagamento = dataPagamento;
        this.valorMediaHoras = valorMediaHoras;
        this.salarioBruto = salarioBruto;
        this.salarioHora = salarioHora;
        this.abonoPecuniario = abonoPecuniario;
        this.adiantar13 = adiantar13;
        this.inicioAquisitivoFerias = inicioAquisitivoFerias;
        this.terminoAquisitivoFerias = terminoAquisitivoFerias;
        this.totalFaltas = totalFaltas;
        this.dataInicioFerias = dataInicioFerias;
        this.terminoFerias = terminoFerias;
        this.totalDiasFerias = totalDiasFerias;
        this.inicioAbono = inicioAbono;
        this.terminoAbono = terminoAbono;
        this.totalDiasAbono = totalDiasAbono;
        this.valorDiasFerias = valorDiasFerias;
        this.totalLiquidoReceber = totalLiquidoReceber;
        this.totalBrutReceber = totalBrutReceber;
        this.totalDeDescontos = totalDeDescontos;
        this.nomeEmpresa = nomeEmpresa;
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public CalculoFerias(CalculoFeriasDto obj) {
        this.id = obj.getId();
        this.numeroMatricula = obj.getNumeroMatricula();
        this.nomeColaborador = obj.getNomeColaborador();
        this.codigoCbo = obj.getCodigoCbo();
        this.centroDeCusto = obj.getCentroDeCusto();
        this.dataDeAdmissao = obj.getDataDeAdmissao();
        this.mesAnoReferencia = obj.getMesAnoReferencia();
        this.mesAnoReferenciaFolha = obj.getMesAnoReferenciaFolha();
        this.tipoDaFolha = obj.getTipoDaFolha();
        this.numeroDependentes = obj.getNumeroDependentes();
        this.dataAvisoFerias = obj.getDataAvisoFerias();
        this.dataReciboFerias = obj.getDataReciboFerias();
        this.dataPagamento = obj.getDataPagamento();
        this.valorMediaHoras = obj.getValorMediaHoras();
        this.salarioBruto = obj.getSalarioBruto();
        this.salarioHora = obj.getSalarioHora();
        this.abonoPecuniario = obj.getAbonoPecuniario();
        this.adiantar13 = obj.getAdiantar13();
        this.inicioAquisitivoFerias = obj.getInicioAquisitivoFerias();
        this.terminoAquisitivoFerias = obj.getTerminoAquisitivoFerias();
        this.totalFaltas = obj.getTotalFaltas();
        this.dataInicioFerias = obj.getDataInicioFerias();
        this.terminoFerias = obj.getTerminoFerias();
        this.totalDiasFerias = obj.getTotalDiasFerias();
        this.inicioAbono = obj.getInicioAbono();
        this.terminoAbono = obj.getTerminoAbono();
        this.totalDiasAbono = obj.getTotalDiasAbono();
        this.valorDiasFerias = obj.getValorDiasFerias();
        this.totalLiquidoReceber = obj.getTotalLiquidoReceber();
        this.totalBrutReceber = obj.getTotalBrutReceber();
        this.totalDeDescontos = obj.getTotalDeDescontos();
        this.nomeEmpresa = obj.getNomeEmpresa();
        this.cnpjEmpresa = obj.getCnpjEmpresa();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CalculoFerias that = (CalculoFerias) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
