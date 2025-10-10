package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResultadoCalculoDto {

    private Integer codigoEvento;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private String descricao;
}
