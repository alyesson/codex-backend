package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Agenda;
import br.com.codex.v1.utilitario.CapitalizarPalavras;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class AgendaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String tituloAgenda;
    protected String nomeReserva;
    protected String sala;
    protected String cor;
    protected LocalDateTime dataHoraInicio;
    protected LocalDateTime dataHoraFim;

    public AgendaDto() {
        super();
    }

    public AgendaDto(Agenda obj) {
        this.id = obj.getId();
        this.tituloAgenda = obj.getTituloAgenda();
        this.nomeReserva = obj.getNomeReserva();
        this.sala = obj.getSala();
        this.cor = obj.getCor();
        this.dataHoraInicio = obj.getDataHoraInicio();
        this.dataHoraFim = obj.getDataHoraFim();
    }

    public void setTituloAgenda(String tituloAgenda) {
        this.tituloAgenda = capitalizarPalavras(tituloAgenda);
    }

}
