package br.com.codex.v1.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class ConciliacaoContabilDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer notaFiscalId;
    private String numeroNota;
    private BigDecimal valorNota;
    private BigDecimal valorLancado;
    private BigDecimal diferenca;
    private boolean conciliado;
    private String observacao;

    public ConciliacaoContabilDto() {
        super();
    }

    public Integer getNotaFiscalId() {
        return notaFiscalId;
    }

    public void setNotaFiscalId(Integer notaFiscalId) {
        this.notaFiscalId = notaFiscalId;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public BigDecimal getValorNota() {
        return valorNota;
    }

    public void setValorNota(BigDecimal valorNota) {
        this.valorNota = valorNota;
    }

    public BigDecimal getValorLancado() {
        return valorLancado;
    }

    public void setValorLancado(BigDecimal valorLancado) {
        this.valorLancado = valorLancado;
    }

    public BigDecimal getDiferenca() {
        return diferenca;
    }

    public void setDiferenca(BigDecimal diferenca) {
        this.diferenca = diferenca;
    }

    public boolean isConciliado() {
        return conciliado;
    }

    public void setConciliado(boolean conciliado) {
        this.conciliado = conciliado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
