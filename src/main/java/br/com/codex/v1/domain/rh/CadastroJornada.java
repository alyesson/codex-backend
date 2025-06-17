package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CadastroJornadaDto;
import br.com.codex.v1.domain.enums.TipoHorario;
import br.com.codex.v1.domain.enums.TipoJornada;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class CadastroJornada implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String codigoJornada;
    private String descricaoJornada;
    private TipoJornada tipoJornada;
    private String diaJornada;
    private TipoHorario tipoHorario;
    private LocalTime inicioExpediente;
    private LocalTime inicioAlmoco;
    private LocalTime fimAlmoco;
    private LocalTime fimExpediente;
    private String jornadaSemanal;
    private String jornadaMensal;
    private String autorAltera;
    private LocalDate ultimaModificacao;

    public CadastroJornada() {
        super();
    }

    public CadastroJornada(Long id, String codigoJornada, String descricaoJornada, TipoJornada tipoJornada, String diaJornada, TipoHorario tipoHorario, LocalTime inicioExpediente, LocalTime inicioAlmoco, LocalTime fimAlmoco, LocalTime fimExpediente, String jornadaSemanal, String jornadaMensal, String autorAltera, LocalDate ultimaModificacao) {
        this.id = id;
        this.codigoJornada = codigoJornada;
        this.descricaoJornada = descricaoJornada;
        this.tipoJornada = tipoJornada;
        this.diaJornada = diaJornada;
        this.tipoHorario = tipoHorario;
        this.inicioExpediente = inicioExpediente;
        this.inicioAlmoco = inicioAlmoco;
        this.fimAlmoco = fimAlmoco;
        this.fimExpediente = fimExpediente;
        this.jornadaSemanal = jornadaSemanal;
        this.jornadaMensal = jornadaMensal;
        this.autorAltera = autorAltera;
        this.ultimaModificacao = ultimaModificacao;
    }

    public CadastroJornada(CadastroJornadaDto obj) {
        this.id = obj.getId();
        this.codigoJornada = obj.getCodigoJornada();
        this.descricaoJornada = obj.getDescricaoJornada();
        this.tipoJornada = TipoJornada.toEnum(obj.getTipoJornada());
        this.diaJornada = obj.getDiaJornada();
        this.tipoHorario = TipoHorario.toEnum(obj.getTipoHorario());
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
        this.descricaoJornada = descricaoJornada;
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(TipoJornada tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public String getDiaJornada() {
        return diaJornada;
    }

    public void setDiaJornada(String diaJornada) {
        this.diaJornada = diaJornada;
    }

    public TipoHorario getTipoHorario() {
        return tipoHorario;
    }

    public void setTipoHorario(TipoHorario tipoHorario) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CadastroJornada that = (CadastroJornada) o;
        return Objects.equals(codigoJornada, that.codigoJornada);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoJornada);
    }
}
