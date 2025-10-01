package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.Aso;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class AsoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotBlank(message = "Nome do funcionário não pode ficar em branco")
    protected String funcionario;
    @NotBlank(message = "Cpf não pode ficar em branco")
    protected String cpf;
    protected Integer idade;
    @NotBlank(message = "Matrícula não pode ficar em branco")
    protected String matricula;
    @NotBlank(message = "Tipo de Aso não pode ficar em branco")
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

    public void setMedicoEmitente(String medicoEmitente) {
        this.medicoEmitente = capitalizarPalavras(medicoEmitente);
    }

    public void setObservacao(String observacao) {
        this.observacao = capitalizarPalavras(observacao);
    }
}
