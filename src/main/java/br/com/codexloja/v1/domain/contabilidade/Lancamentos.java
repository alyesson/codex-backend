package br.com.codexloja.v1.domain.contabilidade;

import br.com.codexloja.v1.domain.cadastros.Pessoa;
import br.com.codexloja.v1.domain.dto.LancamentosDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Lancamentos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected Date dataLancamento;
    protected String formula;
    protected Integer numeroLote;
    protected String historicoPadrao;
    protected String historicoVariavel;
    @JsonIgnore
    @OneToMany(mappedBy = "lancamentos")
    protected List<LancamentosContas> lancamentosContas = new ArrayList<>();

    public Lancamentos() {
        super();
    }

    public Lancamentos(Integer id, Date dataLancamento, String formula, Integer numeroLote, String historicoPadrao, String historicoVariavel) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.formula = formula;
        this.numeroLote = numeroLote;
        this.historicoPadrao = historicoPadrao;
        this.historicoVariavel = historicoVariavel;
    }

    public Lancamentos(LancamentosDto obj) {
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

    public List<LancamentosContas> getLancamentosContas() {
        return lancamentosContas;
    }

    public void setLancamentosContas(List<LancamentosContas> lancamentosContas) {
        this.lancamentosContas = lancamentosContas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lancamentos that = (Lancamentos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
