package br.com.codex.v1.domain.estoque;

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
public class SolicitacaoMaterialItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String descricao;
    private Integer quantidade;
    private String unidadeMedida;
    private String situacao;
    @ManyToOne
    @JoinColumn(name = "solicitacaoMaterial_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SolicitacaoMaterial solicitacaoMaterial;

    public SolicitacaoMaterialItens() {
        super();
    }

    public SolicitacaoMaterialItens(Long id, String codigo, String descricao, Integer quantidade, String unidadeMedida,
                                    String situacao, SolicitacaoMaterial solicitacaoMaterial) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.situacao = situacao;
        this.solicitacaoMaterial = solicitacaoMaterial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SolicitacaoMaterialItens that = (SolicitacaoMaterialItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
