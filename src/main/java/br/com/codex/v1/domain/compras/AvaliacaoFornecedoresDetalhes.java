package br.com.codex.v1.domain.compras;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class AvaliacaoFornecedoresDetalhes implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String criterio;
    protected float peso;
    protected float nota;
    @ManyToOne
    @JoinColumn(name = "avaliacaoFornecedores_id")
    protected AvaliacaoFornecedores avaliacaoFornecedores;

    public AvaliacaoFornecedoresDetalhes() {
        super();
    }

    public AvaliacaoFornecedoresDetalhes(Integer id, String criterio, float peso, float nota, AvaliacaoFornecedores avaliacaoFornecedores) {
        this.id = id;
        this.criterio = criterio;
        this.peso = peso;
        this.nota = nota;
        this.avaliacaoFornecedores = avaliacaoFornecedores;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public AvaliacaoFornecedores getAvaliacaoFornecedores() {
        return avaliacaoFornecedores;
    }

    public void setAvaliacaoFornecedores(AvaliacaoFornecedores avaliacaoFornecedores) {
        this.avaliacaoFornecedores = avaliacaoFornecedores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvaliacaoFornecedoresDetalhes that = (AvaliacaoFornecedoresDetalhes) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
