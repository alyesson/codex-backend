package br.com.codex.v1.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDLPADto {

    private String descricao;
    private BigDecimal valor;
    private String tipo; // "RECEITA", "DESPESA", "DISTRIBUICAO", "AJUSTE"
}
