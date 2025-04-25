package br.com.codex.v1.domain.ti;

import br.com.codex.v1.domain.dto.TiposCategoriasDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class TiposCategorias implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String descricao;
    protected String categoria;

    public TiposCategorias() {
        super();
    }

    public TiposCategorias(Integer id, String descricao, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public TiposCategorias(TiposCategoriasDto obj) {
        this.id = obj.getId();
        this.descricao = obj.getDescricao();
        this.categoria = obj.getCategoria();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposCategorias that = (TiposCategorias) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
