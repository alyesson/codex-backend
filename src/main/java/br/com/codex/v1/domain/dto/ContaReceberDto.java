package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.ContaReceber;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.MinimizarPalavras.minimizarPalavras;

public class ContaReceberDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String descricao;
    protected String categoria;
    protected String recebidoDe;
    protected String numeroDocumento;
    protected String repete;
    protected Date dataVencimento;
    protected Date dataCompetencia;
    protected Date dataEmissao;
    protected Integer quantidadeParcelas;
    protected BigDecimal valor;
    protected String metodoRecebimento;
    protected String situacao;
    protected String observacao;
    protected String origemDocumento;

    public ContaReceberDto() {
        super();
    }

    public ContaReceberDto(ContaReceber obj) {
        this.id = obj.getId();
        this.descricao = obj.getDescricao();
        this.categoria = obj.getCategoria();
        this.recebidoDe = obj.getRecebidoDe();
        this.numeroDocumento = obj.getNumeroDocumento();
        this.repete = obj.getRepete();
        this.dataVencimento = obj.getDataVencimento();
        this.dataCompetencia = obj.getDataCompetencia();
        this.dataEmissao = obj.getDataEmissao();
        this.quantidadeParcelas = obj.getQuantidadeParcelas();
        this.valor = obj.getValor();
        this.metodoRecebimento = obj.getMetodoRecebimento();
        this.situacao = obj.getSituacao();
        this.observacao = obj.getObservacao();
        this.origemDocumento = obj.getOrigemDocumento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getRecebidoDe() {
        return recebidoDe;
    }

    public void setRecebidoDe(String recebidoDe) {
        this.recebidoDe = recebidoDe;
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

    public String getMetodoRecebimento() {
        return metodoRecebimento;
    }

    public void setMetodoRecebimento(String metodoRecebimento) {
        this.metodoRecebimento = metodoRecebimento;
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
        this.observacao = minimizarPalavras(observacao);
    }

    public String getOrigemDocumento() {
        return origemDocumento;
    }

    public void setOrigemDocumento(String origemDocumento) {
        this.origemDocumento = origemDocumento;
    }

}
