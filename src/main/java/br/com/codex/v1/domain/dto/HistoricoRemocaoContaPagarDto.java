package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.HistoricoRemocaoContaPagar;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class HistoricoRemocaoContaPagarDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String motivoRemocao;
    protected Date dataRemocao;
    protected String autor;
    protected Integer numeroLancamento;

    public HistoricoRemocaoContaPagarDto() {
        super();
    }

    public HistoricoRemocaoContaPagarDto(HistoricoRemocaoContaPagar obj) {
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
        this.motivoRemocao = capitalizarPalavras(motivoRemocao);
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
}
