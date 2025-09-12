package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemRelatorioDto {

    private String nome;
    private BigDecimal valor;
    private String tipo; // "GRUPO" ou "ITEM"

    public ItemRelatorioDto() {
    }

    public ItemRelatorioDto(String nome, BigDecimal valor, String tipo) {
        this.nome = nome;
        this.valor = valor;
        this.tipo = tipo;
    }
}
