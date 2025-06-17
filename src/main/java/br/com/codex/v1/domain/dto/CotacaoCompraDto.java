package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class CotacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Integer numeroSolicitacao;
    protected String solicitante;
    protected Date dataSolicitacao;
    protected Date dataAbertura;
    protected Date validade;
    protected String situacao;
    protected String comprador;
    @NotNull(message = "O fornecedor não pode estar em branco")
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String cep;
    protected String contato;
    protected BigDecimal valorCotado;
    protected List<CotacaoItensCompraDto> itens;
    protected String link;

    public CotacaoCompraDto() {
        super();
    }

    public CotacaoCompraDto(CotacaoCompra obj) {
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

    public List<CotacaoItensCompraDto> getItens() {
        return itens;
    }

    public void setItens(List<CotacaoItensCompraDto> itens) {
        this.itens = itens;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
