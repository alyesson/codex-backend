package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Lancamentos;
import br.com.codex.v1.domain.contabilidade.LancamentosContas;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class LancamentosContasDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected Date dataLancamento;
    protected String contaDebito;
    protected String contaCredito;
    protected String centroCustoDebito;
    protected String centroCustoCredito;
    protected BigDecimal valorDebito;
    protected BigDecimal valorCredito;
    protected LancamentosDto lancamentos;
    protected String origem;

    public LancamentosContasDto() {
        super();
    }

    public LancamentosContasDto(LancamentosContas obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.contaDebito = obj.getCentroCustoDebito();
        this.contaCredito = obj.getContaCredito();
        this.centroCustoDebito = obj.getCentroCustoDebito();
        this.centroCustoCredito = obj.getCentroCustoCredito();
        this.valorDebito = obj.getValorDebito();
        this.valorCredito = obj.getValorCredito();
        this.lancamentos = new LancamentosDto(obj.getLancamentos());
        this.origem = obj.getOrigem();
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

    public String getContaDebito() {
        return contaDebito;
    }

    public void setContaDebito(String contaDebito) {
        this.contaDebito = contaDebito;
    }

    public String getContaCredito() {
        return contaCredito;
    }

    public void setContaCredito(String contaCredito) {
        this.contaCredito = contaCredito;
    }

    public String getCentroCustoDebito() {
        return centroCustoDebito;
    }

    public void setCentroCustoDebito(String centroCustoDebito) {
        this.centroCustoDebito = centroCustoDebito;
    }

    public String getCentroCustoCredito() {
        return centroCustoCredito;
    }

    public void setCentroCustoCredito(String centroCustoCredito) {
        this.centroCustoCredito = centroCustoCredito;
    }

    public BigDecimal getValorDebito() {
        return valorDebito;
    }

    public void setValorDebito(BigDecimal valorDebito) {
        this.valorDebito = valorDebito;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public LancamentosDto getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(LancamentosDto lancamentos) {
        this.lancamentos = lancamentos;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
