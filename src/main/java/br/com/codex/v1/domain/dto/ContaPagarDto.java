package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.ContaPagar;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class ContaPagarDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String descricao;
    protected String categoria;
    protected String pagoA;
    protected String numeroDocumento;
    protected String repete;
    protected Date dataVencimento;
    protected Date dataCompetencia;
    protected Date dataEmissao;
    protected Integer quantidadeParcelas;
    protected BigDecimal valor;
    protected String metodoPagamento;
    protected String situacao;
    protected String observacao;
    protected String origemDocumento;

    public ContaPagarDto() {
        super();
    }

    public ContaPagarDto(ContaPagar obj) {
        this.id = obj.getId();
        this.descricao = obj.getDescricao();
        this.categoria = obj.getCategoria();
        this.pagoA = obj.getPagoA();
        this.numeroDocumento = obj.getNumeroDocumento();
        this.repete = obj.getRepete();
        this.dataVencimento = obj.getDataVencimento();
        this.dataCompetencia = obj.getDataCompetencia();
        this.dataEmissao = obj.getDataEmissao();
        this.quantidadeParcelas = obj.getQuantidadeParcelas();
        this.valor = obj.getValor();
        this.metodoPagamento = obj.getMetodoPagamento();
        this.situacao = obj.getSituacao();
        this.observacao = obj.getObservacao();
        this.origemDocumento = obj.getOrigemDocumento();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPagoA() {
        return pagoA;
    }

    public void setPagoA(String pagoA) {
        this.pagoA = pagoA;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getRepete() {
        return repete;
    }

    public void setRepete(String repete) {
        this.repete = repete;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataCompetencia() {
        return dataCompetencia;
    }

    public void setDataCompetencia(Date dataCompetencia) {
        this.dataCompetencia = dataCompetencia;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getOrigemDocumento() {
        return origemDocumento;
    }

    public void setOrigemDocumento(String origemDocumento) {
        this.origemDocumento = origemDocumento;
    }
}
