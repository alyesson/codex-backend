package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.CotacaoCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class CotacaoCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Integer numeroSolicitacao;
    protected String solicitante;
    protected Date dataSolicitacao;
    protected Date dataAbertura;
    protected Date validade;
    protected String situacao;
    protected String comprador;
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String cep;
    protected String contato;
    protected BigDecimal valorCotado;
    @JsonIgnore
    @OneToMany(mappedBy = "cotacaoCompra")
    protected List<CotacaoItensCompra> cotacaoItensCompras = new ArrayList<>();
    protected String link;

    public CotacaoCompra() {
        super();
    }

    public CotacaoCompra(Long id, Integer numeroSolicitacao, String solicitante, Date dataSolicitacao, Date dataAbertura, Date validade, String situacao, String comprador, String fornecedor, String cnpj, String ie, String endereco, String cep, String contato, BigDecimal valorCotado, String link) {
        this.id = id;
        this.numeroSolicitacao = numeroSolicitacao;
        this.solicitante = solicitante;
        this.dataSolicitacao = dataSolicitacao;
        this.dataAbertura = dataAbertura;
        this.validade = validade;
        this.situacao = situacao;
        this.comprador = comprador;
        this.fornecedor = fornecedor;
        this.cnpj = cnpj;
        this.ie = ie;
        this.endereco = endereco;
        this.cep = cep;
        this.contato = contato;
        this.valorCotado = valorCotado;
        this.link = link;
    }

    public CotacaoCompra(CotacaoCompraDto obj) {
        this.id = obj.getId();
        this.numeroSolicitacao = obj.getNumeroSolicitacao();
        this.solicitante = obj.getSolicitante();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.dataAbertura = obj.getDataAbertura();
        this.validade = obj.getValidade();
        this.situacao = obj.getSituacao();
        this.comprador = obj.getComprador();
        this.fornecedor = obj.getFornecedor();
        this.cnpj = obj.getCnpj();
        this.ie = obj.getIe();
        this.endereco = obj.getEndereco();
        this.cep = obj.getCep();
        this.contato = obj.getContato();
        this.valorCotado = obj.getValorCotado();
        this.link = obj.getLink();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroSolicitacao() {
        return numeroSolicitacao;
    }

    public void setNumeroSolicitacao(Integer numeroSolicitacao) {
        this.numeroSolicitacao = numeroSolicitacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public BigDecimal getValorCotado() {
        return valorCotado;
    }

    public void setValorCotado(BigDecimal valorCotado) {
        this.valorCotado = valorCotado;
    }

    public List<CotacaoItensCompra> getCotacaoItensCompras() {
        return cotacaoItensCompras;
    }

    public void setCotacaoItensCompras(List<CotacaoItensCompra> cotacaoItensCompras) {
        this.cotacaoItensCompras = cotacaoItensCompras;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CotacaoCompra that = (CotacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
