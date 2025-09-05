package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.DepartamentoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Departamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigo;
    protected String descricao;
    protected String centroCusto;

    public Departamento() {
        super();
    }

    public Departamento(Long id, String codigo, String descricao, String centroCusto) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.centroCusto = centroCusto;
    }

    public Departamento(DepartamentoDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.centroCusto = obj.getCentroCusto();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;
        return Objects.equals(id, that.id) && Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
