package br.com.codex.v1.domain.ti;

import br.com.codex.v1.domain.dto.AtendimentosDto;
import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Atendimentos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String solicitante;
    protected String email;
    protected String telefone;
    protected String departamento;
    protected String titulo;
    protected String problema;
    protected String categoria;
    protected String tipo;
    protected Prioridade prioridade;
    protected LocalDate dataAbertura;
    protected LocalDate dataFechamento;
    protected String diasAtuacao;
    protected String horaInicio;
    protected String horaFim;
    protected String resolucao;
    @Column(columnDefinition = "BYTEA")
    protected byte[] imagem;
    protected String nomeTecnico;
    protected Situacao situacao;

    public Atendimentos() {
        super();
    }

    public Atendimentos(Long id, String solicitante, String email, String telefone, String departamento, String titulo, String problema, String categoria, String tipo, Prioridade prioridade, LocalDate dataAbertura, LocalDate dataFechamento, String diasAtuacao, String horaInicio, String horaFim, String resolucao, byte[] imagem, String nomeTecnico, Situacao situacao) {
        super();
        this.id = id;
        this.solicitante = solicitante;
        this.email = email;
        this.telefone = telefone;
        this.departamento = departamento;
        this.titulo = titulo;
        this.problema = problema;
        this.categoria = categoria;
        this.tipo = tipo;
        this.prioridade = prioridade;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.diasAtuacao = diasAtuacao;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.resolucao = resolucao;
        this.imagem = imagem;
        this.nomeTecnico = nomeTecnico;
        this.situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atendimentos that = (Atendimentos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
