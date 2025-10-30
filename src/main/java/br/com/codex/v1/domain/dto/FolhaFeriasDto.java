package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaFerias;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FolhaFeriasDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "O número da matrícula não pode ser nulo")
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
    private BigDecimal valorMediaHoras;
    private BigDecimal salarioBruto;
    private BigDecimal salarioHora;
    private String abonoPecuniario;
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
    private BigDecimal valorDiasFerias;
    private BigDecimal totalLiquidoReceber;
    private BigDecimal totalBrutReceber;
    private BigDecimal totalDeDescontos;
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private List<FolhaFeriasEventosDto> eventos;

    public FolhaFeriasDto() {
        super();
    }

    public FolhaFeriasDto(FolhaFerias obj) {
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
}
