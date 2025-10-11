package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.SolicitacaoMaterialDto;
import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private LocalDate dataSolicitacao;
    private LocalDate dataEntrega;
    private Situacao situacao;
    private Prioridade prioridade;
    private String localEntrega;
    private String observacao;
    private String motivoSolicitacao;

    @JsonIgnore
    @OneToMany(mappedBy = "solicitacaoMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitacaoMaterialItens> SolicitacaoMaterialItens = new ArrayList<>();

    public SolicitacaoMaterial() {
        super();
    }

    public SolicitacaoMaterial(Long id, String solicitante, String email, String departamento,
                               String centroCusto, LocalDate dataSolicitacao, LocalDate dataEntrega,
                               Situacao situacao, Prioridade prioridade, String localEntrega,
                               String observacao, String motivoSolicitacao) {
        this.id = id;
        this.solicitante = solicitante;
        this.email = email;
        this.departamento = departamento;
        this.centroCusto = centroCusto;
        this.dataSolicitacao = dataSolicitacao;
        this.dataEntrega = dataEntrega;
        this.situacao = situacao;
        this.prioridade = prioridade;
        this.localEntrega = localEntrega;
        this.observacao = observacao;
        this.motivoSolicitacao = motivoSolicitacao;
    }

    public SolicitacaoMaterial(SolicitacaoMaterialDto obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.email = obj.getEmail();
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.dataEntrega = obj.getDataEntrega();
        this.situacao = obj.getSituacao();
        this.prioridade = obj.getPrioridade();
        this.localEntrega = obj.getLocalEntrega();
        this.observacao = obj.getObservacao();
        this.motivoSolicitacao = obj.getMotivoSolicitacao();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SolicitacaoMaterial that = (SolicitacaoMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
