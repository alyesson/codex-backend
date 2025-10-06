package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProcessamentoFolhaDto {
    private boolean sucesso;
    private String mensagem;
    private LocalDateTime dataProcessamento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private List<TotaisFolhaPontoDto> totaisColaboradores = new ArrayList<>();
    private List<InconsistenciaPontoDto> inconsistencias = new ArrayList<>();

    // Totais gerais
    private Long totalHorasTrabalhadasMinutos;
    private Long totalHorasExtrasMinutos;
    private Long totalHorasFaltantesMinutos;
    private BigDecimal totalCustoHorasExtras;
    private Integer totalColaboradoresProcessados;

    public ProcessamentoFolhaDto() {
        this.dataProcessamento = LocalDateTime.now();
    }

    public Duration getTotalHorasTrabalhadas() {
        return totalHorasTrabalhadasMinutos != null ? Duration.ofMinutes(totalHorasTrabalhadasMinutos) : Duration.ZERO;
    }

    public Duration getTotalHorasExtras() {
        return totalHorasExtrasMinutos != null ? Duration.ofMinutes(totalHorasExtrasMinutos) : Duration.ZERO;
    }

    public Duration getTotalHorasFaltantes() {
        return totalHorasFaltantesMinutos != null ? Duration.ofMinutes(totalHorasFaltantesMinutos) : Duration.ZERO;
    }
}
