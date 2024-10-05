package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.SubGrupoDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class SubGrupo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String codigoGrupo;
    protected String codigoSubGrupo;
    protected String descricao;

    public SubGrupo() {
        super();
    }

    public SubGrupo(Integer id, String codigoGrupo, String codigoSubGrupo, String descricao) {
        this.id = id;
        this.codigoGrupo = codigoGrupo;
        this.codigoSubGrupo = codigoSubGrupo;
        this.descricao = descricao;
    }

    public SubGrupo(SubGrupoDto obj) {
        this.id = obj.getId();
        this.codigoGrupo = obj.getCodigoGrupo();
        this.codigoSubGrupo = obj.getCodigoSubGrupo();
        this.descricao = obj.getDescricao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getCodigoSubGrupo() {
        return codigoSubGrupo;
    }

    public void setCodigoSubGrupo(String codigoSubGrupo) {
        this.codigoSubGrupo = codigoSubGrupo;
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
        SubGrupo that = (SubGrupo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

