package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class SolicitacaoMaterial implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String solicitante;
    private String email;
    private String departamento;
    private String centroCusto;
    private String dataSolicitacao;
    private Situacao situacao;
    private Prioridade prioridade;
    private String localEntrega;
    private String observacao;
    private String motivoSolicitacao;

    @JsonIgnore
    @OneToMany(mappedBy = "solicitacaoMaterial")
    private List<SolicitacaoMaterialItens> SolicitacaoMaterialItens = new ArrayList<>();

    public SolicitacaoMaterial() {
        super();
    }

    public SolicitacaoMaterial(Long id, String solicitante, String email, String departamento,
                               String centroCusto, String dataSolicitacao, Situacao situacao, Prioridade prioridade,
                               String localEntrega, String observacao, String motivoSolicitacao) {
        this.id = id;
        this.solicitante = solicitante;
        this.email = email;
        this.departamento = departamento;
        this.centroCusto = centroCusto;
        this.dataSolicitacao = dataSolicitacao;
        this.situacao = situacao;
        this.prioridade = prioridade;
        this.localEntrega = localEntrega;
        this.observacao = observacao;
        this.motivoSolicitacao = motivoSolicitacao;
    }
}
