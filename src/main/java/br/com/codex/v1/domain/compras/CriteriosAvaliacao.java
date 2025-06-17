package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.CriteriosAvaliacaoDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class CriteriosAvaliacao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String criterio;
    protected float peso;
    protected String descricao;

    public CriteriosAvaliacao() {
        super();
    }

    public CriteriosAvaliacao(Long id, String criterio, float peso, String descricao) {
        this.id = id;
        this.criterio = criterio;
        this.peso = peso;
        this.descricao = descricao;
    }

    public CriteriosAvaliacao(CriteriosAvaliacaoDto obj) {
        this.id = obj.getId();
        this.criterio = obj.getCriterio();
        this.peso = obj.getPeso();
        this.descricao = obj.getDescricao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriteriosAvaliacao that = (CriteriosAvaliacao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
