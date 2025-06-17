package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.AsoDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Aso implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(length = 60)
    protected String funcionario;
    @Column(length = 15)
    protected String cpf;
    @Column(length = 2)
    protected Integer idade;
    @Column(length = 5)
    protected String matricula;
    @Column(length = 50)
    protected String tipoAso;
    @Column(length = 60)
    protected String cargo;
    @Column(length = 60)
    protected String medicoEmitente;
    @Column(length = 6)
    protected String crm;
    @Column(length = 10)
    protected Date dataInicio;
    @Column(length = 10)
    protected Date dataFim;
    @Column(length = 10)
    protected Date dataRealizacao;
    @Column(length = 20)
    protected String situacaoAtual;
    protected String observacao;

    public Aso() {
        super();
    }

    public Aso(Long id, String funcionario, String cpf,Integer idade, String matricula, String tipoAso, String cargo, String medicoEmitente, String crm, Date dataInicio, Date dataFim, Date dataRealizacao, String situacaoAtual, String observacao) {
        this.id = id;
        this.funcionario = funcionario;
        this.cpf = cpf;
        this.idade = idade;
        this.matricula = matricula;
        this.tipoAso = tipoAso;
        this.cargo = cargo;
        this.medicoEmitente = medicoEmitente;
        this.crm = crm;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataRealizacao = dataRealizacao;
        this.situacaoAtual = situacaoAtual;
        this.observacao = observacao;
    }

    public Aso(AsoDto obj) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aso aso = (Aso) o;
        return Objects.equals(id, aso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
