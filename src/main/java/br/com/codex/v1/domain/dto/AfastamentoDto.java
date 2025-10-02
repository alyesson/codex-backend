package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.Afastamento;
import br.com.codex.v1.utilitario.MaximizarPalavras;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static br.com.codex.v1.utilitario.MaximizarPalavras.maximizarPalavras;

@Getter
@Setter
public class AfastamentoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "Número do contrato/matrícula não pode ficar em branco")
    private String contrato;
    @NotBlank(message = "Nome do funcionário não pode ficar em branco")
    private String funcionario;
    @NotBlank(message = "Número do cpf não pode ficar em branco")
    private String cpf;
    private String estabilidade;
    @NotBlank(message = "Crm do médico não pode ficar em branco")
    private String crm;
    @NotBlank(message = "Nome do médico não pode ficar em branco")
    private String medico;
    @NotBlank(message = "Cid não pode ficar em branco")
    private String cid;
    @NotBlank(message = "Código do afastamento não pode ficar em branco")
    private String codigoMotivoAfastamento;
    @NotBlank(message = "Motivo do afastamento não pode ficar em branco")
    private String motivoAfastamento;
    @NotNull(message = "Data do afastamento não pode ficar em branco")
    private LocalDate dataAfastamento;
    private LocalDate dataPrevistaRetorno;
    private Integer diasAfastado;
    private String eventoEsocial;
    private String origemRetificacao;
    private String numeroProcesso;
    private String observacao;

    public AfastamentoDto() {
        super();
    }

    public AfastamentoDto(Afastamento obj) {
        this.id = obj.getId();
        this.contrato = obj.getContrato();
        this.funcionario = obj.getFuncionario();
        this.cpf = obj.getCpf();
        this.estabilidade = obj.getEstabilidade();
        this.crm = obj.getCrm();
        this.medico = obj.getMedico();
        this.cid = obj.getCid();
        this.codigoMotivoAfastamento = obj.getCodigoMotivoAfastamento();
        this.motivoAfastamento = obj.getMotivoAfastamento();
        this.dataAfastamento = obj.getDataAfastamento();
        this.dataPrevistaRetorno = obj.getDataPrevistaRetorno();
        this.diasAfastado = obj.getDiasAfastado();
        this.eventoEsocial = obj.getEventoEsocial();
        this.origemRetificacao = obj.getOrigemRetificacao();
        this.numeroProcesso = obj.getNumeroProcesso();
        this.observacao = obj.getObservacao();
    }

    public void setEventoEsocial(String eventoEsocial) {
        this.eventoEsocial = maximizarPalavras(eventoEsocial);
    }
}
