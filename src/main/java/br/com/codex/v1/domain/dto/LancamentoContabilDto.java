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
    private Contas contaDebito;
    private Contas contaCredito;
    private HistoricoPadrao historicoPadrao;
    private NotasFiscais notaFiscalOrigem;
    private String complementoHistorico; // opcional, se quiser adicionar infos extras

    public LancamentoContabilDto() {
    }

    public LancamentoContabilDto(LancamentoContabil obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.valor = obj.getValor();
        this.contaDebito = obj.getContaDebito();
        this.contaCredito = obj.getContaCredito();
        this.historicoPadrao = obj.getHistoricoPadrao();
        this.notaFiscalOrigem = obj.getNotaFiscalOrigem();
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

    public Contas getContaDebito() {
        return contaDebito;
    }

    public void setContaDebito(Contas contaDebito) {
        this.contaDebito = contaDebito;
    }

    public Contas getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(Contas contaCredito) {
        this.contaCredito = contaCredito;
    }

    public HistoricoPadrao getHistoricoPadrao() {
        return historicoPadrao;
    }

    public void setHistoricoPadrao(HistoricoPadrao historicoPadrao) {
        this.historicoPadrao = historicoPadrao;
    }

    public NotasFiscais getNotaFiscalOrigem() {
        return notaFiscalOrigem;
    }

    public void setNotaFiscalOrigem(NotasFiscais notaFiscalOrigem) {
        this.notaFiscalOrigem = notaFiscalOrigem;
    }

    public String getComplementoHistorico() {
        return complementoHistorico;
    }

    public void setComplementoHistorico(String complementoHistorico) {
        this.complementoHistorico = complementoHistorico;
    }
}
