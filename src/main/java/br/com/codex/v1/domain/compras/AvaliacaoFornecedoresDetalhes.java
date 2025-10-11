package br.com.codex.v1.domain.compras;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AvaliacaoFornecedoresDetalhes implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String criterio;
    protected float peso;
    protected float nota;

    @ManyToOne
    @JoinColumn(name = "avaliacaoFornecedores_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected AvaliacaoFornecedores avaliacaoFornecedores;

    public AvaliacaoFornecedoresDetalhes() {
        super();
    }

    public AvaliacaoFornecedoresDetalhes(Long id, String criterio, float peso, float nota, AvaliacaoFornecedores avaliacaoFornecedores) {
        this.id = id;
        this.criterio = criterio;
        this.peso = peso;
        this.nota = nota;
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
