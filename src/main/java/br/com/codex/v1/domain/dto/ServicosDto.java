package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Servicos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ServicosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String codigo;
    protected String nome;
    protected String descricao;
    protected String nbsCodigo;
    protected String nbsNome;
    protected String cnaeCodigo;
    protected String cnaeNome;
    protected BigDecimal valorVista;
    protected BigDecimal valorPrazo;
    protected BigDecimal baseCalculo;           // Base sobre a qual os tributos incidem
    protected BigDecimal aliquotaPercentual;    // Alíquota total (se quiser um campo agregador)
    protected BigDecimal issPercentual;
    protected BigDecimal issValor;
    protected BigDecimal pisPercentual;
    protected BigDecimal pisValor;
    protected BigDecimal cofinsPercentual;
    protected BigDecimal cofinsValor;
    protected BigDecimal csllPercentual;
    protected BigDecimal cssValor;
    protected BigDecimal valorTotalServico;

    public ServicosDto() {
        super();
    }

    public ServicosDto(Servicos obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.nome = obj.getNome();
        this.descricao = obj.getDescricao();
        this.nbsCodigo = obj.getNbsCodigo();
        this.nbsNome = obj.getNbsNome();
        this.cnaeCodigo = obj.getCnaeCodigo();
        this.cnaeNome = obj.getCnaeNome();
        this.valorVista = obj.getValorVista();
        this.valorPrazo = obj.getValorPrazo();
        this.baseCalculo = obj.getBaseCalculo();
        this.aliquotaPercentual = obj.getAliquotaPercentual();
        this.issPercentual = obj.getIssPercentual();
        this.issValor = obj.getIssValor();
        this.pisPercentual = obj.getPisPercentual();
        this.pisValor = obj.getPisValor();
        this.cofinsPercentual = obj.getCofinsPercentual();
        this.cofinsValor = obj.getCofinsValor();
        this.csllPercentual = obj.getCsllPercentual();
        this.cssValor = obj.getCssValor();
        this.valorTotalServico = obj.getValorTotalServico();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNbsCodigo() {
        return nbsCodigo;
    }

    public void setNbsCodigo(String nbsCodigo) {
        this.nbsCodigo = nbsCodigo;
    }

    public String getNbsNome() {
        return nbsNome;
    }

    public void setNbsNome(String nbsNome) {
        this.nbsNome = nbsNome;
    }

    public String getCnaeCodigo() {
        return cnaeCodigo;
    }

    public void setCnaeCodigo(String cnaeCodigo) {
        this.cnaeCodigo = cnaeCodigo;
    }

    public String getCnaeNome() {
        return cnaeNome;
    }

    public void setCnaeNome(String cnaeNome) {
        this.cnaeNome = cnaeNome;
    }

    public BigDecimal getValorVista() {
        return valorVista;
    }

    public void setValorVista(BigDecimal valorVista) {
        this.valorVista = valorVista;
    }

    public BigDecimal getValorPrazo() {
        return valorPrazo;
    }

    public void setValorPrazo(BigDecimal valorPrazo) {
        this.valorPrazo = valorPrazo;
    }

    public BigDecimal getBaseCalculo() {
        return baseCalculo;
    }

    public void setBaseCalculo(BigDecimal baseCalculo) {
        this.baseCalculo = baseCalculo;
    }

    public BigDecimal getAliquotaPercentual() {
        return aliquotaPercentual;
    }

    public void setAliquotaPercentual(BigDecimal aliquotaPercentual) {
        this.aliquotaPercentual = aliquotaPercentual;
    }

    public BigDecimal getIssPercentual() {
        return issPercentual;
    }

    public void setIssPercentual(BigDecimal issPercentual) {
        this.issPercentual = issPercentual;
    }

    public BigDecimal getIssValor() {
        return issValor;
    }

    public void setIssValor(BigDecimal issValor) {
        this.issValor = issValor;
    }

    public BigDecimal getPisPercentual() {
        return pisPercentual;
    }

    public void setPisPercentual(BigDecimal pisPercentual) {
        this.pisPercentual = pisPercentual;
    }

    public BigDecimal getPisValor() {
        return pisValor;
    }

    public void setPisValor(BigDecimal pisValor) {
        this.pisValor = pisValor;
    }

    public BigDecimal getCofinsPercentual() {
        return cofinsPercentual;
    }

    public void setCofinsPercentual(BigDecimal cofinsPercentual) {
        this.cofinsPercentual = cofinsPercentual;
    }

    public BigDecimal getCofinsValor() {
        return cofinsValor;
    }

    public void setCofinsValor(BigDecimal cofinsValor) {
        this.cofinsValor = cofinsValor;
    }

    public BigDecimal getCsllPercentual() {
        return csllPercentual;
    }

    public void setCsllPercentual(BigDecimal csllPercentual) {
        this.csllPercentual = csllPercentual;
    }

    public BigDecimal getCssValor() {
        return cssValor;
    }

    public void setCssValor(BigDecimal cssValor) {
        this.cssValor = cssValor;
    }

    public BigDecimal getValorTotalServico() {
        return valorTotalServico;
    }

    public void setValorTotalServico(BigDecimal valorTotalServico) {
        this.valorTotalServico = valorTotalServico;
    }
}
