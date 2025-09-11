package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BalanceteDto {

    private List<ContaBalanceteDto> contas;
    private ResumoBalanceteDto resumo;
    private String nivelDetalhe;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    // Construtores, getters e setters
    public BalanceteDto() {
    }

    public BalanceteDto(List<ContaBalanceteDto> contas, ResumoBalanceteDto resumo,
                        String nivelDetalhe, LocalDate dataInicial, LocalDate dataFinal) {
        this.contas = contas;
        this.resumo = resumo;
        this.nivelDetalhe = nivelDetalhe;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }
}
