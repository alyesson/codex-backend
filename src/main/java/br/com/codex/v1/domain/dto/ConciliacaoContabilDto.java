package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ConciliacaoContabilDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long notaFiscalId;
    private String numeroNota;
    private BigDecimal valorNota;
    private BigDecimal valorLancado;
    private BigDecimal diferenca;
    private boolean conciliado;
    private String observacao;
    private String conta;

    public ConciliacaoContabilDto() {
        super();
    }
}
