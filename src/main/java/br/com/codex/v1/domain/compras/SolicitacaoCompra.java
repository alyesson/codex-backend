package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
public class SolicitacaoCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String solicitante;
    protected String departamento;
    protected Date dataSolicitacao;
    protected String centroCusto;
    protected String motivoCompra;
    protected String destinoMaterial;
    protected String eUrgente;
    protected String opcaoMarca;
    protected String eItemEstoque;
    @JsonIgnore
    @OneToMany(mappedBy = "solicitacaoCompra")
    protected List<SolicitacaoItensCompra> solicitacaoItensCompra = new ArrayList<>();
    protected String situacao;

    public SolicitacaoCompra() {
        super();
    }

    public SolicitacaoCompra(Long id, String solicitante, String departamento, Date dataSolicitacao, String centroCusto, String motivoCompra, String destinoMaterial, String eUrgente, String opcaoMarca, String eItemEstoque, String situacao) {
        this.id = id;
        this.solicitante = solicitante;
        this.departamento = departamento;
        this.dataSolicitacao = dataSolicitacao;
        this.centroCusto = centroCusto;
        this.motivoCompra = motivoCompra;
        this.destinoMaterial = destinoMaterial;
        this.eUrgente = eUrgente;
        this.opcaoMarca = opcaoMarca;
        this.eItemEstoque = eItemEstoque;
        this.situacao  = situacao;
    }

    public SolicitacaoCompra(SolicitacaoCompraDto obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.departamento = obj.getDepartamento();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.centroCusto = obj.getCentroCusto();
        this.motivoCompra = obj.getMotivoCompra();
        this.destinoMaterial = obj.getDestinoMaterial();
        this.eUrgente = obj.geteUrgente();
        this.opcaoMarca = obj.getOpcaoMarca();
        this.eItemEstoque = obj.geteItemEstoque();
        this.situacao = obj.getSituacao();
    }
    
    public void setMotivoCompra(String motivoCompra) {
        this.motivoCompra = capitalizarPalavras(motivoCompra);
    }

    public void setDestinoMaterial(String destinoMaterial) {
        this.destinoMaterial = capitalizarPalavras(destinoMaterial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitacaoCompra that = (SolicitacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
