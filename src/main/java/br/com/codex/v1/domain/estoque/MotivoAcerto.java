package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.MotivoAcertoDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class MotivoAcerto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String codigo;
    protected String descricao;

    public MotivoAcerto() {
        super();
    }

    public MotivoAcerto(Integer id, String codigo, String descricao) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public MotivoAcerto(MotivoAcertoDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        MotivoAcerto that = (MotivoAcerto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
