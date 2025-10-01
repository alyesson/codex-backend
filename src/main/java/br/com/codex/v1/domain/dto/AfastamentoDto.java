package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.Afastamento;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class AfastamentoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String contrato;
    private String funcionario;
    private String cpf;
    private String estabilidade;
    private String crm;
    private String medico;
    private String cid;
    private String codigoMotivoAfastamento;
    private String motivoAfastamento;
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
}
