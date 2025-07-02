package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroJornada;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class CadastroJornadaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O código da jornada não pode estar em branco")
    protected String codigoJornada;
    protected String descricaoJornada;
    protected Integer tipoJornada;
    protected String diaJornada;
    protected Integer tipoHorario;
    protected LocalTime inicioExpediente;
    protected LocalTime inicioAlmoco;
    protected LocalTime fimAlmoco;
    protected LocalTime fimExpediente;
    protected String jornadaSemanal;
    protected String jornadaMensal;
    protected String autorAltera;
    protected LocalDate ultimaModificacao;

    public CadastroJornadaDto() {
        super();
    }

    public CadastroJornadaDto(CadastroJornada obj) {
        this.id = obj.getId();
        this.codigoJornada = obj.getCodigoJornada();
        this.descricaoJornada = obj.getDescricaoJornada();
        this.tipoJornada = obj.getTipoJornada().getCodigo();
        this.diaJornada = obj.getDiaJornada();
        this.tipoHorario = obj.getTipoHorario().getCodigo();
        this.inicioExpediente = obj.getInicioExpediente();
        this.inicioAlmoco = obj.getInicioAlmoco();
        this.fimAlmoco = obj.getFimAlmoco();
        this.fimExpediente = obj.getFimExpediente();
        this.jornadaSemanal = obj.getJornadaSemanal();
        this.jornadaMensal = obj.getJornadaMensal();
        this.autorAltera = obj.getAutorAltera();
        this.ultimaModificacao = obj.getUltimaModificacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoJornada() {
        return codigoJornada;
    }

    public void setCodigoJornada(String codigoJornada) {
        this.codigoJornada = codigoJornada;
    }

    public String getDescricaoJornada() {
        return descricaoJornada;
    }

    public void setDescricaoJornada(String descricaoJornada) {
        this.descricaoJornada = capitalizarPalavras(descricaoJornada);
    }

    public Integer getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(Integer tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public String getDiaJornada() {
        return diaJornada;
    }

    public void setDiaJornada(String diaJornada) {
        this.diaJornada = diaJornada;
    }

    public Integer getTipoHorario() {
        return tipoHorario;
    }

    public void setTipoHorario(Integer tipoHorario) {
        this.tipoHorario = tipoHorario;
    }

    public LocalTime getInicioExpediente() {
        return inicioExpediente;
    }

    public void setInicioExpediente(LocalTime inicioExpediente) {
        this.inicioExpediente = inicioExpediente;
    }

    public LocalTime getInicioAlmoco() {
        return inicioAlmoco;
    }

    public void setInicioAlmoco(LocalTime inicioAlmoco) {
        this.inicioAlmoco = inicioAlmoco;
    }

    public LocalTime getFimAlmoco() {
        return fimAlmoco;
    }

    public void setFimAlmoco(LocalTime fimAlmoco) {
        this.fimAlmoco = fimAlmoco;
    }

    public LocalTime getFimExpediente() {
        return fimExpediente;
    }

    public void setFimExpediente(LocalTime fimExpediente) {
        this.fimExpediente = fimExpediente;
    }

    public String getJornadaSemanal() {
        return jornadaSemanal;
    }

    public void setJornadaSemanal(String jornadaSemanal) {
        this.jornadaSemanal = jornadaSemanal;
    }

    public String getJornadaMensal() {
        return jornadaMensal;
    }

    public void setJornadaMensal(String jornadaMensal) {
        this.jornadaMensal = jornadaMensal;
    }

    public String getAutorAltera() {
        return autorAltera;
    }

    public void setAutorAltera(String autorAltera) {
        this.autorAltera = autorAltera;
    }

    public LocalDate getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(LocalDate ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }
}
