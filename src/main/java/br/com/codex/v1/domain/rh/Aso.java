package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.AsoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
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

    public void setObservacao(String observacao) {
        this.observacao = capitalizarPalavras(observacao);
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
