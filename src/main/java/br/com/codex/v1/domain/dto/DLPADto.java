package br.com.codex.v1.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DLPADto {

    private List<ItemDLPADto> itens;
    private BigDecimal saldoInicial;
    private BigDecimal lucroPrejuizoPeriodo;
    private BigDecimal distribuicoes;
    private BigDecimal ajustes;
    private BigDecimal saldoFinal;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String empresaNome;
}
