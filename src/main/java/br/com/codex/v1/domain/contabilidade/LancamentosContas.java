package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.cadastros.Pessoa;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class LancamentosContas implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected Date dataLancamento;
    protected String contaDebito;
    protected String contaCredito;
    protected String centroCustoDebito;
    protected String centroCustoCredito;
    protected BigDecimal valorDebito;
    protected BigDecimal valorCredito;
    @ManyToOne
    @JoinColumn(name = "lancamentos_id")
    protected Lancamentos lancamentos;
    protected String origem;

    public LancamentosContas() {
        super();
    }

    public LancamentosContas(Integer id, Date dataLancamento, String contaDebito, String contaCredito, String centroCustoDebito, String centroCustoCredito, BigDecimal valorDebito, BigDecimal valorCredito, Lancamentos lancamentos, String origem) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.contaDebito = contaDebito;
        this.contaCredito = contaCredito;
        this.centroCustoDebito = centroCustoDebito;
        this.centroCustoCredito = centroCustoCredito;
        this.valorDebito = valorDebito;
        this.valorCredito = valorCredito;
        this.lancamentos = lancamentos;
        this.origem = origem;
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

    public Lancamentos getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(Lancamentos lancamentos) {
        this.lancamentos = lancamentos;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LancamentosContas that = (LancamentosContas) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
