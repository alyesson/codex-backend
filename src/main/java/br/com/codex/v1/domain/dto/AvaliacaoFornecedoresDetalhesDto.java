package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;

import java.io.Serial;
import java.io.Serializable;

public class AvaliacaoFornecedoresDetalhesDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String criterio;
    protected float peso;
    protected float nota;
    protected AvaliacaoFornecedoresDto avaliacaoFornecedores;

    public AvaliacaoFornecedoresDetalhesDto() {
        super();
    }

    public AvaliacaoFornecedoresDetalhesDto(AvaliacaoFornecedoresDetalhes obj) {
        this.id = obj.getId();
        this.criterio = obj.getCriterio();
        this.peso = obj.getPeso();
        this.nota = obj.getNota();
        this.avaliacaoFornecedores = new AvaliacaoFornecedoresDto(obj.getAvaliacaoFornecedores());
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

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public AvaliacaoFornecedoresDto getAvaliacaoFornecedores() {
        return avaliacaoFornecedores;
    }

    public void setAvaliacaoFornecedores(AvaliacaoFornecedoresDto avaliacaoFornecedores) {
        this.avaliacaoFornecedores = avaliacaoFornecedores;
    }
}
