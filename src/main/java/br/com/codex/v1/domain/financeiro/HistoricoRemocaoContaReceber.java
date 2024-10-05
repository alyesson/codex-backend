package br.com.codex.v1.domain.financeiro;

import br.com.codex.v1.domain.dto.HistoricoRemocaoContaReceberDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class HistoricoRemocaoContaReceber implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String motivoRemocao;
    protected Date dataRemocao;
    protected String autor;
    protected Integer numeroLancamento;

    public HistoricoRemocaoContaReceber() {
        super();
    }

    public HistoricoRemocaoContaReceber(Integer id, String motivoRemocao, Date dataRemocao, String autor, Integer numeroLancamento) {
        this.id = id;
        this.motivoRemocao = motivoRemocao;
        this.dataRemocao = dataRemocao;
        this.autor = autor;
        this.numeroLancamento = numeroLancamento;
    }

    public HistoricoRemocaoContaReceber(HistoricoRemocaoContaReceberDto obj) {
        this.id = obj.getId();
        this.motivoRemocao = obj.getMotivoRemocao();
        this.dataRemocao = obj.getDataRemocao();
        this.autor = obj.getAutor();
        this.numeroLancamento = obj.getNumeroLancamento();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotivoRemocao() {
        return motivoRemocao;
    }

    public void setMotivoRemocao(String motivoRemocao) {
        this.motivoRemocao = motivoRemocao;
    }

    public Date getDataRemocao() {
        return dataRemocao;
    }

    public void setDataRemocao(Date dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getNumeroLancamento() {
        return numeroLancamento;
    }

    public void setNumeroLancamento(Integer numeroLancamento) {
        this.numeroLancamento = numeroLancamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoRemocaoContaReceber that = (HistoricoRemocaoContaReceber) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
