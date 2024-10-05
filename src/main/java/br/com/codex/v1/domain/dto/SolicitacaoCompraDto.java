package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class SolicitacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String solicitante;
    protected String departamento;
    protected Date dataSolicitacao;
    @NotNull(message = "Centro de custo não pode estar em branco")
    protected String centroCusto;
    protected String motivoCompra;
    protected String destinoMaterial;
    protected String eUrgente;
    protected String opcaoMarca;
    protected String eItemEstoque;
    protected List<SolicitacaoItensCompraDto> itens;
    protected String situacao;

    public SolicitacaoCompraDto() {
        super();
    }

    public SolicitacaoCompraDto(SolicitacaoCompra obj) {
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

    public List<SolicitacaoItensCompraDto> getItens() {
        return itens;
    }

    public void setItens(List<SolicitacaoItensCompraDto> itens) {
        this.itens = itens;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
