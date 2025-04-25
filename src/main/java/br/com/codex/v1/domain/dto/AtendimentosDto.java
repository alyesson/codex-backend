package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.ti.Atendimentos;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

public class AtendimentosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String solicitante;
    @Email
    protected String email;
    protected String telefone;
    protected String departamento;
    protected String titulo;
    @NotNull(message = "A descrição do problema não pode star em branco")
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    protected String problema;
    protected String categoria;
    protected String tipo;
    protected Integer prioridade;
    protected LocalDate dataAbertura;
    protected LocalDate dataFechamento;
    protected String diasAtuacao;
    protected String horaInicio;
    protected String horaFim;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    protected String resolucao;
    private byte[] imagem;
    protected String nomeTecnico;
    protected Integer situacao;

    public AtendimentosDto() {
        super();
    }

    public AtendimentosDto(Atendimentos obj) {
        this.id = obj.getId();
        this.solicitante = obj.getSolicitante();
        this.email = obj.getEmail();
        this.telefone = obj.getTelefone();
        this.departamento = obj.getDepartamento();
        this.titulo = obj.getTitulo();
        this.problema = obj.getProblema();
        this.categoria = obj.getCategoria();
        this.tipo = obj.getTipo();
        this.prioridade = obj.getPrioridade().getCodigo();
        this.dataAbertura = obj.getDataAbertura();
        this.dataFechamento = obj.getDataFechamento();
        this.diasAtuacao = obj.getDiasAtuacao();
        this.horaInicio = obj.getHoraInicio();
        this.horaFim = obj.getHoraFim();
        this.resolucao = obj.getResolucao();
        this.imagem = obj.getImagem();
        this.nomeTecnico = obj.getNomeTecnico();
        this.situacao = obj.getSituacao().getCodigo();
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

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
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

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }
}
