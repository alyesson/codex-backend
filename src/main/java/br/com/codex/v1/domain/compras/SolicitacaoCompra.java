package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SolicitacaoCompra implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
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

    public SolicitacaoCompra(Integer id, String solicitante, String departamento, Date dataSolicitacao, String centroCusto, String motivoCompra, String destinoMaterial, String eUrgente, String opcaoMarca, String eItemEstoque, String situacao) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public String getMotivoCompra() {
        return motivoCompra;
    }

    public void setMotivoCompra(String motivoCompra) {
        this.motivoCompra = motivoCompra;
    }

    public String getDestinoMaterial() {
        return destinoMaterial;
    }

    public void setDestinoMaterial(String destinoMaterial) {
        this.destinoMaterial = destinoMaterial;
    }

    public String geteUrgente() {
        return eUrgente;
    }

    public void seteUrgente(String eUrgente) {
        this.eUrgente = eUrgente;
    }

    public String getOpcaoMarca() {
        return opcaoMarca;
    }

    public void setOpcaoMarca(String opcaoMarca) {
        this.opcaoMarca = opcaoMarca;
    }

    public String geteItemEstoque() {
        return eItemEstoque;
    }

    public void seteItemEstoque(String eItemEstoque) {
        this.eItemEstoque = eItemEstoque;
    }

    public List<SolicitacaoItensCompra> getSolicitacaoItensCompra() {
        return solicitacaoItensCompra;
    }

    public void setSolicitacaoItensCompra(List<SolicitacaoItensCompra> solicitacaoItensCompra) {
        this.solicitacaoItensCompra = solicitacaoItensCompra;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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
