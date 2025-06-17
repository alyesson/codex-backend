package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.SalaReuniao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class SalaReuniaoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String nome;
    protected String localizacao;

    public SalaReuniaoDto() {
        super();
    }

    public SalaReuniaoDto(SalaReuniao obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.localizacao = obj.getLocalizacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SalaReuniaoDto that = (SalaReuniaoDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
