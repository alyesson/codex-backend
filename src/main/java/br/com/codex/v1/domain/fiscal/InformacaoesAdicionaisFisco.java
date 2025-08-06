package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.dto.InformacaoesAdicionaisFiscoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class InformacaoesAdicionaisFisco implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String codigo;

    @Lob
    private String descricao;

    public InformacaoesAdicionaisFisco() {
        super();
    }

    public InformacaoesAdicionaisFisco(Long id, String codigo, String descricao) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public InformacaoesAdicionaisFisco(InformacaoesAdicionaisFiscoDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InformacaoesAdicionaisFisco that = (InformacaoesAdicionaisFisco) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
