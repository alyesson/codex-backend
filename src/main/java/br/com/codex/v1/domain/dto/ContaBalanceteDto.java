package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ContaBalanceteDto {

    private String codigo;
    private String descricao;
    private BigDecimal debito;
    private BigDecimal credito;
    private BigDecimal saldo;
    private Integer nivel;

    // Construtores, getters e setters
    public ContaBalanceteDto() {
    }

    public ContaBalanceteDto(String codigo, String descricao, BigDecimal debito,
                             BigDecimal credito, BigDecimal saldo, Integer nivel) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.debito = debito;
        this.credito = credito;
        this.saldo = saldo;
        this.nivel = nivel;
    }
}
