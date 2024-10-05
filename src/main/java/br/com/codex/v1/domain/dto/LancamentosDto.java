package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Lancamentos;
import br.com.codex.v1.domain.contabilidade.LancamentosContas;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LancamentosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected Date dataLancamento;
    protected String formula;
    protected Integer numeroLote;
    protected String historicoPadrao;
    protected String historicoVariavel;
    protected List<LancamentosContasDto> contas;

    public LancamentosDto() {
        super();
    }

    public LancamentosDto(Lancamentos obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.formula = obj.getFormula();
        this.numeroLote = obj.getNumeroLote();
        this.historicoPadrao = obj.getHistoricoPadrao();
        this.historicoVariavel = obj.getHistoricoVariavel();
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

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getHistoricoPadrao() {
        return historicoPadrao;
    }

    public void setHistoricoPadrao(String historicoPadrao) {
        this.historicoPadrao = historicoPadrao;
    }

    public String getHistoricoVariavel() {
        return historicoVariavel;
    }

    public void setHistoricoVariavel(String historicoVariavel) {
        this.historicoVariavel = historicoVariavel;
    }

    public List<LancamentosContasDto> getContas() {
        return contas;
    }

    public void setContas(List<LancamentosContasDto> contas) {
        this.contas = contas;
    }
}
