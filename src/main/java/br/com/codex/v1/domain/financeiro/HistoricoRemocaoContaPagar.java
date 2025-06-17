package br.com.codex.v1.domain.financeiro;

import br.com.codex.v1.domain.dto.HistoricoRemocaoContaPagarDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class HistoricoRemocaoContaPagar implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String motivoRemocao;
    protected Date dataRemocao;
    protected String autor;
    protected Integer numeroLancamento;

    public HistoricoRemocaoContaPagar() {
        super();
    }

    public HistoricoRemocaoContaPagar(Long id, String motivoRemocao, Date dataRemocao, String autor, Integer numeroLancamento) {
        this.id = id;
        this.motivoRemocao = motivoRemocao;
        this.dataRemocao = dataRemocao;
        this.autor = autor;
        this.numeroLancamento = numeroLancamento;
    }

    public HistoricoRemocaoContaPagar(HistoricoRemocaoContaPagarDto obj) {
        this.id = obj.getId();
        this.motivoRemocao = obj.getMotivoRemocao();
        this.dataRemocao = obj.getDataRemocao();
        this.autor = obj.getAutor();
        this.numeroLancamento = obj.getNumeroLancamento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        HistoricoRemocaoContaPagar that = (HistoricoRemocaoContaPagar) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
