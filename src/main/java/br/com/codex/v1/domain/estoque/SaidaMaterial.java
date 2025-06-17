package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.SaidaMaterialDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class SaidaMaterial implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigoProduto;
    protected String produto;
    protected String lote;
    protected Date dataSaida;
    protected Integer quantidade;
    protected String autor;
    protected String motivoAcerto;
    protected String solicitante;

    public SaidaMaterial() {
        super();
    }

    public SaidaMaterial(Long id, String codigoProduto, String produto, String motivoAcerto, String lote, Date dataSaida, Integer quantidade, String autor, String solicitante) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.motivoAcerto = motivoAcerto;
        this.produto = produto;
        this.lote = lote;
        this.dataSaida = dataSaida;
        this.quantidade = quantidade;
        this.autor = autor;
        this.solicitante = solicitante;
    }

    public SaidaMaterial(SaidaMaterialDto obj) {
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

    public String getProduto() {
        return produto;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getMotivoAcerto() {
        return motivoAcerto;
    }

    public void setMotivoAcerto(String motivoAcerto) {
        this.motivoAcerto = motivoAcerto;
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
    
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaidaMaterial that = (SaidaMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
