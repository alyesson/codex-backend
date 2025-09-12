package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DFCDto {
    private List<FluxoCaixaItemDto> fluxoOperacional;
    private List<FluxoCaixaItemDto> fluxoInvestimento;
    private List<FluxoCaixaItemDto> fluxoFinanciamento;

    private BigDecimal totalOperacional;
    private BigDecimal totalInvestimento;
    private BigDecimal totalFinanciamento;

    private BigDecimal saldoInicial;
    private BigDecimal variacaoPeriodo;
    private BigDecimal saldoFinal;

    private LocalDate dataInicial;
    private LocalDate dataFinal;

    // Construtores, getters e setters
    public DFCDto() {
    }

    public DFCDto(List<FluxoCaixaItemDto> fluxoOperacional, List<FluxoCaixaItemDto> fluxoInvestimento,
                  List<FluxoCaixaItemDto> fluxoFinanciamento, BigDecimal totalOperacional,
                  BigDecimal totalInvestimento, BigDecimal totalFinanciamento, BigDecimal saldoInicial,
                  BigDecimal variacaoPeriodo, BigDecimal saldoFinal, LocalDate dataInicial, LocalDate dataFinal) {
        this.fluxoOperacional = fluxoOperacional;
        this.fluxoInvestimento = fluxoInvestimento;
        this.fluxoFinanciamento = fluxoFinanciamento;
        this.totalOperacional = totalOperacional;
        this.totalInvestimento = totalInvestimento;
        this.totalFinanciamento = totalFinanciamento;
        this.saldoInicial = saldoInicial;
        this.variacaoPeriodo = variacaoPeriodo;
        this.saldoFinal = saldoFinal;
        this.dataInicial = dataInicial;
        this.dataFinal = dataInicial;
    }
}