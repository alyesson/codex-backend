package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.EntradaMaterial;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;


public class EntradaMaterialDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    protected Integer id;
    @NotNull(message = "Código do produto não pode estar em branco")
    protected String codigoProduto;
    @NotNull(message = "Descrição do produto não pode estar em branco")
    protected String produto;
    protected Date dataEntrada;
    protected String fornecedor;
    protected String cnpjFornecedor;
    protected Integer quantidade;
    protected Integer notaFiscal;
    protected Date dataNota;
    protected BigDecimal valorProduto;
    protected BigDecimal valorNota;
    protected String lote;
    protected String validade;
    protected String autor;
    protected String motivoAcerto;
    protected String unidadeComercial;


    public EntradaMaterialDto() {
        super();
    }

    public EntradaMaterialDto(EntradaMaterial obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.produto = obj.getProduto();
        this.dataEntrada = obj.getDataEntrada();
        this.fornecedor = obj.getFornecedor();
        this.cnpjFornecedor = obj.getCnpjFornecedor();
        this.quantidade = obj.getQuantidade();
        this.notaFiscal = obj.getNotaFiscal();
        this.dataNota = obj.getDataNota();
        this.valorProduto = obj.getValorProduto();
        this.valorNota = obj.getValorNota();
        this.lote = obj.getLote();
        this.validade = obj.getValidade();
        this.autor = obj.getAutor();
        this.motivoAcerto = obj.getMotivoAcerto();
        this.unidadeComercial = obj.getUnidadeComercial();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(Integer notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public Date getDataNota() {
        return dataNota;
    }

    public void setDataNota(Date dataNota) {
        this.dataNota = dataNota;
    }

    public BigDecimal getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(BigDecimal valorProduto) {
        this.valorProduto = valorProduto;
    }

    public BigDecimal getValorNota() {
        return valorNota;
    }

    public void setValorNota(BigDecimal valorNota) {
        this.valorNota = valorNota;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getAutor() {
        return autor;
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

    public String getUnidadeComercial() {
        return unidadeComercial;
    }

    public void setUnidadeComercial(String unidadeComercial) {
        this.unidadeComercial = unidadeComercial;
    }
}
