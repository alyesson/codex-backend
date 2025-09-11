package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FluxoCaixaItemDto {

    private String descricao;
    private BigDecimal valor;

    public FluxoCaixaItemDto() {
    }

    public FluxoCaixaItemDto(String descricao, BigDecimal valor) {
        this.descricao = descricao;
        this.valor = valor;
    }
}
