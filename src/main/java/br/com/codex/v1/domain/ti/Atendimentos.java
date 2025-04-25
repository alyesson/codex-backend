package br.com.codex.v1.domain.ti;

import br.com.codex.v1.domain.dto.AtendimentosDto;
import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Atendimentos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
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
    @Lob
    protected byte[] imagem;
    protected String nomeTecnico;
    protected Situacao situacao;

    public Atendimentos() {
        super();
    }

    public Atendimentos(Integer id, String solicitante, String email, String telefone, String departamento, String titulo, String problema, String categoria, String tipo, Prioridade prioridade, LocalDate dataAbertura, LocalDate dataFechamento, String diasAtuacao, String horaInicio, String horaFim, String resolucao, byte[] imagem, String nomeTecnico, Situacao situacao) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDate dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getDiasAtuacao() {
        return diasAtuacao;
    }

    public void setDiasAtuacao(String diasAtuacao) {
        this.diasAtuacao = diasAtuacao;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getResolucao() {
        return resolucao;
    }

    public void setResolucao(String resolucao) {
        this.resolucao = resolucao;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getNomeTecnico() {
        return nomeTecnico;
    }

    public void setNomeTecnico(String nomeTecnico) {
        this.nomeTecnico = nomeTecnico;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
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
