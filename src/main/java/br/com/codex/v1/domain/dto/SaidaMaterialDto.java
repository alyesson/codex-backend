package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.SaidaMaterial;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

public class SaidaMaterialDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigoProduto;
    protected String produto;
    protected String lote;
    protected Date dataSaida;
    protected Integer quantidade;
    protected String autor;
    protected String motivoAcerto;
    protected String solicitante;

    public SaidaMaterialDto() {
        super();
    }

    public SaidaMaterialDto(SaidaMaterial obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.motivoAcerto = obj.getMotivoAcerto();
        this.produto = obj.getProduto();
        this.lote = obj.getLote();
        this.dataSaida = obj.getDataSaida();
        this.quantidade = obj.getQuantidade();
        this.autor = obj.getAutor();
        this.solicitante = obj.getSolicitante();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getMotivoAcerto() {
        return motivoAcerto;
    }

    public void setMotivoAcerto(String motivoAcerto) {
        this.motivoAcerto = motivoAcerto;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
}
