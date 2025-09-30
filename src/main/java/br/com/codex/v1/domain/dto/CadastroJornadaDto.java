package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroJornada;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.RemoveEspacoBranco.removerEspacos;

@Getter
@Setter
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

    public void setCodigoJornada(String codigoJornada) {
        this.codigoJornada = removerEspacos(codigoJornada);
    }

    public void setDescricaoJornada(String descricaoJornada) {
        this.descricaoJornada = capitalizarPalavras(descricaoJornada);
    }
}
