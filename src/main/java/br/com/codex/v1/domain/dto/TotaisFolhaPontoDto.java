package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
public class TotaisFolhaPontoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long colaboradorId;
    private String nomeColaborador;
    private String pis;
    private Long totalHorasTrabalhadasMinutos;
    private Long totalHorasExtrasMinutos;
    private Long totalHorasFaltantesMinutos;
    private BigDecimal totalCustoHorasExtras;

    public TotaisFolhaPontoDto(Long colaboradorId, String nomeColaborador, String pis,
                               Long totalHorasTrabalhadasMinutos, Long totalHorasExtrasMinutos,
                               Long totalHorasFaltantesMinutos, BigDecimal totalCustoHorasExtras) {
        this.colaboradorId = colaboradorId;
        this.nomeColaborador = nomeColaborador;
        this.pis = pis;
        this.totalHorasTrabalhadasMinutos = totalHorasTrabalhadasMinutos != null ? totalHorasTrabalhadasMinutos : 0L;
        this.totalHorasExtrasMinutos = totalHorasExtrasMinutos != null ? totalHorasExtrasMinutos : 0L;
        this.totalHorasFaltantesMinutos = totalHorasFaltantesMinutos != null ? totalHorasFaltantesMinutos : 0L;
        this.totalCustoHorasExtras = totalCustoHorasExtras != null ? totalCustoHorasExtras : BigDecimal.ZERO;
    }

    public Duration getTotalHorasTrabalhadas() {
        return Duration.ofMinutes(totalHorasTrabalhadasMinutos);
    }

    public Duration getTotalHorasExtras() {
        return Duration.ofMinutes(totalHorasExtrasMinutos);
    }

    public Duration getTotalHorasFaltantes() {
        return Duration.ofMinutes(totalHorasFaltantesMinutos);
    }
}