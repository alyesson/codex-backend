package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.LancamentoContabilDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class LancamentoContabil implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dataLancamento;

    @Column(precision = 20, scale = 2)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "conta_debito_id")
    private Contas contaDebito;

    @ManyToOne
    @JoinColumn(name = "conta_credito_id")
    private Contas contaCredito;

    @ManyToOne
    @JoinColumn(name = "historico_id")
    private HistoricoPadrao historicoPadrao;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id")
    private ImportarXml notaFiscalOrigem;

    private String complementoHistorico; // opcional, se quiser adicionar infos extras

    public LancamentoContabil() {
    }

    public LancamentoContabil(Long id, Date dataLancamento, BigDecimal valor, Contas contaDebito, Contas contaCredito, HistoricoPadrao historicoPadrao, ImportarXml notaFiscalOrigem, String complementoHistorico) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.valor = valor;
        this.contaDebito = contaDebito;
        this.contaCredito = contaCredito;
        this.historicoPadrao = historicoPadrao;
        this.notaFiscalOrigem = notaFiscalOrigem;
        this.complementoHistorico = complementoHistorico;
    }

    public LancamentoContabil(LancamentoContabilDto obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.valor = obj.getValor();
        this.complementoHistorico = obj.getComplementoHistorico();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ImportarXml getNotaFiscalOrigem() {
        return notaFiscalOrigem;
    }

    public void setNotaFiscalOrigem(ImportarXml notaFiscalOrigem) {
        this.notaFiscalOrigem = notaFiscalOrigem;
    }

    public String getComplementoHistorico() {
        return complementoHistorico;
    }

    public void setComplementoHistorico(String complementoHistorico) {
        this.complementoHistorico = complementoHistorico;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LancamentoContabil that = (LancamentoContabil) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
