package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.ti.TiposCategorias;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class TiposCategoriasDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    protected Long id;
    protected String descricao;
    protected String categoria;

    public TiposCategoriasDto() {
        super();
    }

    public TiposCategoriasDto(TiposCategorias obj) {
        this.id = obj.getId();
        this.descricao = obj.getDescricao();
        this.categoria = obj.getCategoria();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
