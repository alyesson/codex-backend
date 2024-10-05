package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.HistoricoMovimentacaoMaterialDto;

import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class HistoricoMovimentacaoMaterial implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String codigoProduto;
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

    public HistoricoMovimentacaoMaterial() {
        super();
    }

    public HistoricoMovimentacaoMaterial(Integer id, String codigoProduto, String produto, Date dataEntrada, String fornecedor, String cnpjFornecedor, Integer quantidade, Integer notaFiscal, Date dataNota, BigDecimal valorProduto, BigDecimal valorNota, String lote, String validade, String autor, String motivoAcerto, String unidadeComercial) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.produto = produto;
        this.dataEntrada = dataEntrada;
        this.fornecedor = fornecedor;
        this.cnpjFornecedor = cnpjFornecedor;
        this.quantidade = quantidade;
        this.notaFiscal = notaFiscal;
        this.dataNota = dataNota;
        this.valorProduto = valorProduto;
        this.valorNota = valorNota;
        this.lote = lote;
        this.validade = validade;
        this.autor = autor;
        this.motivoAcerto = motivoAcerto;
        this.unidadeComercial = unidadeComercial;
    }

    public HistoricoMovimentacaoMaterial(HistoricoMovimentacaoMaterialDto obj) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoMovimentacaoMaterial that = (HistoricoMovimentacaoMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
