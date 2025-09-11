package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResumoBalanceteDto {

    private BigDecimal totalDebito;
    private BigDecimal totalCredito;
    private BigDecimal saldoFinal;

    // Construtores, getters e setters
    public ResumoBalanceteDto() {
    }

    public ResumoBalanceteDto(BigDecimal totalDebito, BigDecimal totalCredito, BigDecimal saldoFinal) {
        this.totalDebito = totalDebito;
        this.totalCredito = totalCredito;
        this.saldoFinal = saldoFinal;
    }
}
