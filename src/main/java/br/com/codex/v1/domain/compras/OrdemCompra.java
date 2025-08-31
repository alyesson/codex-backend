package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.OrdemCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class OrdemCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String solicitante;
    protected String departamento;
    protected LocalDate dataSolicitacao;
    protected String centroCusto;
    protected String motivoCompra;
    protected String destinoMaterial;
    protected String urgente;
    protected String opcaoMarca;
    protected String itemEstoque;
    @JsonIgnore
    @OneToMany(mappedBy = "ordemCompra")
    protected List<OrdemItensCompra> ordemItensCompra = new ArrayList<>();
    protected String situacao;

    public OrdemCompra() {
        super();
    }

    public OrdemCompra(Long id, String solicitante, String departamento, LocalDate dataSolicitacao,
                       String centroCusto, String motivoCompra, String destinoMaterial, String urgente,
                       String opcaoMarca, String itemEstoque, String situacao) {
        this.id = id;
        this.solicitante = solicitante;
        this.departamento = departamento;
        this.dataSolicitacao = dataSolicitacao;
        this.centroCusto = centroCusto;
        this.motivoCompra = motivoCompra;
        this.destinoMaterial = destinoMaterial;
        this.urgente = urgente;
        this.opcaoMarca = opcaoMarca;
        this.itemEstoque = itemEstoque;
        this.situacao  = situacao;
    }

    public OrdemCompra(OrdemCompraDto obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.departamento = obj.getDepartamento();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.centroCusto = obj.getCentroCusto();
        this.motivoCompra = obj.getMotivoCompra();
        this.destinoMaterial = obj.getDestinoMaterial();
        this.urgente = obj.getUrgente();
        this.opcaoMarca = obj.getOpcaoMarca();
        this.itemEstoque = obj.getItemEstoque();
        this.situacao = obj.getSituacao();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemCompra that = (OrdemCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
