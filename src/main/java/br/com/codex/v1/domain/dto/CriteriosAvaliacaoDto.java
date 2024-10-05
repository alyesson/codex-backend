package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.CriteriosAvaliacao;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class CriteriosAvaliacaoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    @NotNull(message = "O nome do critério não pode ficar em branco")
    protected String criterio;
    @NotNull(message = "O peso do critério não pode ficar em branco")
    protected float peso;
    protected String descricao;

    public CriteriosAvaliacaoDto() {
        super();
    }

    public CriteriosAvaliacaoDto(CriteriosAvaliacao obj) {
        this.id = obj.getId();
        this.criterio = obj.getCriterio();
        this.peso = obj.getPeso();
        this.descricao = obj.getDescricao();
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
        this.criterio = capitalizarPalavras(criterio);
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
        this.descricao = capitalizarPalavras(descricao);
    }
}
