package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class SolicitacaoMaterialDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String solicitante;
    private String email;
    private String departamento;
    private String centroCusto;
    private LocalDate dataSolicitacao;
    private Situacao situacao;
    private Prioridade prioridade;
    private String localEntrega;
    private String observacao;
    private String motivoSolicitacao;
    private List<SolicitacaoMaterialItensDto> itens;

    public SolicitacaoMaterialDto() {
        super();
    }

    public SolicitacaoMaterialDto(SolicitacaoMaterial obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.email = obj.getEmail();
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.situacao = obj.getSituacao();
        this.prioridade = obj.getPrioridade();
        this.localEntrega = obj.getLocalEntrega();
        this.observacao = obj.getObservacao();
        this.motivoSolicitacao = obj.getMotivoSolicitacao();
    }

    public void setObservacao(String observacao) {
        this.observacao = capitalizarPalavras(observacao);
    }

    public void setMotivoSolicitacao(String motivoSolicitacao) {
        this.motivoSolicitacao = capitalizarPalavras(motivoSolicitacao);
    }
}
