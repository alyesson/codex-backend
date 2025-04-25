package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Contas;
import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.estoque.NotasFiscais;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;


public class LancamentoContabilDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date dataLancamento;
    private BigDecimal valor;
    private Integer contaDebito;
    private Integer contaCredito;
    private Integer historicoPadrao;
    private Integer notaFiscalOrigem;
    private String complementoHistorico; // opcional, se quiser adicionar infos extras

    public LancamentoContabilDto() {
    }

    public LancamentoContabilDto(LancamentoContabil obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.valor = obj.getValor();
        this.contaDebito = obj.getContaDebito() != null ? obj.getContaDebito().getId() : null;
        this.contaCredito = obj.getContaCredito() != null ? obj.getContaCredito().getId() : null;
        this.historicoPadrao = obj.getHistoricoPadrao() != null ? obj.getHistoricoPadrao().getId() : null;
        this.notaFiscalOrigem = obj.getNotaFiscalOrigem() != null ? obj.getNotaFiscalOrigem().getId() : null;
        this.complementoHistorico = obj.getComplementoHistorico();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getContaDebito() {
        return contaDebito;
    }

    public void setContaDebito(Integer contaDebito) {
        this.contaDebito = contaDebito;
    }

    public Integer getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(Integer contaCredito) {
        this.contaCredito = contaCredito;
    }

    public Integer getHistoricoPadrao() {
        return historicoPadrao;
    }

    public void setHistoricoPadrao(Integer historicoPadrao) {
        this.historicoPadrao = historicoPadrao;
    }

    public Integer getNotaFiscalOrigem() {
        return notaFiscalOrigem;
    }

    public void setNotaFiscalOrigem(Integer notaFiscalOrigem) {
        this.notaFiscalOrigem = notaFiscalOrigem;
    }

    public String getComplementoHistorico() {
        return complementoHistorico;
    }

    public void setComplementoHistorico(String complementoHistorico) {
        this.complementoHistorico = complementoHistorico;
    }
}
