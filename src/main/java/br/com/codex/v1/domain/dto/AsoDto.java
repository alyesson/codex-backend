package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.Aso;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class AsoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "Nome do funcionário não pode ficar em branco")
    protected String funcionario;
    @NotNull(message = "Cpf não pode ficar em branco")
    protected String cpf;
    protected Integer idade;
    @NotNull(message = "Matrícula não pode ficar em branco")
    protected String matricula;
    @NotNull(message = "Tipo de Aso não pode ficar em branco")
    protected String tipoAso;
    protected String cargo;
    protected String medicoEmitente;
    protected String crm;
    protected Date dataInicio;
    protected Date dataFim;
    protected Date dataRealizacao;
    protected String situacaoAtual;
    protected String observacao;

    public AsoDto() {
        super();
    }

    public AsoDto(Aso obj) {
        this.id = obj.getId();
        this.funcionario = obj.getFuncionario();
        this.cpf = obj.getCpf();
        this.idade = obj.getIdade();
        this.matricula = obj.getMatricula();
        this.tipoAso = obj.getTipoAso();
        this.cargo = obj.getCargo();
        this.medicoEmitente = obj.getMedicoEmitente();
        this.crm = obj.getCrm();
        this.dataInicio = obj.getDataInicio();
        this.dataFim = obj.getDataFim();
        this.dataRealizacao = obj.getDataRealizacao();
        this.situacaoAtual = obj.getSituacaoAtual();
        this.observacao = obj.getObservacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipoAso() {
        return tipoAso;
    }

    public void setTipoAso(String tipoAso) {
        this.tipoAso = tipoAso;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getMedicoEmitente() {
        return medicoEmitente;
    }

    public void setMedicoEmitente(String medicoEmitente) {
        this.medicoEmitente = medicoEmitente;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(Date dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
