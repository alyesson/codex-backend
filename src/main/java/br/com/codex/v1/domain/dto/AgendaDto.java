package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Agenda;
import br.com.codex.v1.utilitario.CapitalizarPalavras;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class AgendaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTituloAgenda() {
        return tituloAgenda;
    }

    public void setTituloAgenda(String tituloAgenda) {
        this.tituloAgenda = capitalizarPalavras(tituloAgenda);
    }

    public String getNomeReserva() {
        return nomeReserva;
    }

    public void setNomeReserva(String nomeReserva) {
        this.nomeReserva = nomeReserva;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

}
