package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.AfastamentoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Afastamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(columnDefinition = "TEXT")
    private String observacao;

    public Afastamento() {
        super();
    }

    public Afastamento(Long id, String contrato, String funcionario, String cpf, String estabilidade, String crm, String medico,
                       String cid, String codigoMotivoAfastamento, String motivoAfastamento, LocalDate dataAfastamento,
                       LocalDate dataPrevistaRetorno, Integer diasAfastado, String eventoEsocial, String origemRetificacao,
                       String numeroProcesso, String observacao) {
        this.id = id;
        this.contrato = contrato;
        this.funcionario = funcionario;
        this.cpf = cpf;
        this.estabilidade = estabilidade;
        this.crm = crm;
        this.medico = medico;
        this.cid = cid;
        this.codigoMotivoAfastamento = codigoMotivoAfastamento;
        this.motivoAfastamento = motivoAfastamento;
        this.dataAfastamento = dataAfastamento;
        this.dataPrevistaRetorno = dataPrevistaRetorno;
        this.diasAfastado = diasAfastado;
        this.eventoEsocial = eventoEsocial;
        this.origemRetificacao = origemRetificacao;
        this.numeroProcesso = numeroProcesso;
        this.observacao = observacao;
    }

    public Afastamento(AfastamentoDto obj) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Afastamento that = (Afastamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
