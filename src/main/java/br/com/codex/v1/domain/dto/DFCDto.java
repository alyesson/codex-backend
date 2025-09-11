package br.com.codex.v1.domain.dto;

import java.math.BigDecimal;
import java.util.List;

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

    // Construtores, getters e setters
    public DFCDto() {
    }

    public DFCDto(List<FluxoCaixaItemDto> fluxoOperacional, List<FluxoCaixaItemDto> fluxoInvestimento,
                  List<FluxoCaixaItemDto> fluxoFinanciamento, BigDecimal totalOperacional,
                  BigDecimal totalInvestimento, BigDecimal totalFinanciamento, BigDecimal saldoInicial,
                  BigDecimal variacaoPeriodo, BigDecimal saldoFinal) {
        this.fluxoOperacional = fluxoOperacional;
        this.fluxoInvestimento = fluxoInvestimento;
        this.fluxoFinanciamento = fluxoFinanciamento;
        this.totalOperacional = totalOperacional;
        this.totalInvestimento = totalInvestimento;
        this.totalFinanciamento = totalFinanciamento;
        this.saldoInicial = saldoInicial;
        this.variacaoPeriodo = variacaoPeriodo;
        this.saldoFinal = saldoFinal;
    }

    public List<FluxoCaixaItemDto> getFluxoOperacional() {
        return fluxoOperacional;
    }

    public void setFluxoOperacional(List<FluxoCaixaItemDto> fluxoOperacional) {
        this.fluxoOperacional = fluxoOperacional;
    }

    public List<FluxoCaixaItemDto> getFluxoInvestimento() {
        return fluxoInvestimento;
    }

    public void setFluxoInvestimento(List<FluxoCaixaItemDto> fluxoInvestimento) {
        this.fluxoInvestimento = fluxoInvestimento;
    }

    public List<FluxoCaixaItemDto> getFluxoFinanciamento() {
        return fluxoFinanciamento;
    }

    public void setFluxoFinanciamento(List<FluxoCaixaItemDto> fluxoFinanciamento) {
        this.fluxoFinanciamento = fluxoFinanciamento;
    }

    public BigDecimal getTotalOperacional() {
        return totalOperacional;
    }

    public void setTotalOperacional(BigDecimal totalOperacional) {
        this.totalOperacional = totalOperacional;
    }

    public BigDecimal getTotalInvestimento() {
        return totalInvestimento;
    }

    public void setTotalInvestimento(BigDecimal totalInvestimento) {
        this.totalInvestimento = totalInvestimento;
    }

    public BigDecimal getTotalFinanciamento() {
        return totalFinanciamento;
    }

    public void setTotalFinanciamento(BigDecimal totalFinanciamento) {
        this.totalFinanciamento = totalFinanciamento;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getVariacaoPeriodo() {
        return variacaoPeriodo;
    }

    public void setVariacaoPeriodo(BigDecimal variacaoPeriodo) {
        this.variacaoPeriodo = variacaoPeriodo;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }
}