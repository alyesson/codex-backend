package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.AgendaDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Agenda implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String tituloAgenda;
    protected String nomeReserva;
    protected String sala;
    protected String cor;
    protected LocalDateTime dataHoraInicio;
    protected LocalDateTime dataHoraFim;

    public Agenda() {
        super();
    }

    public Agenda(Integer id, String tituloAgenda, String nomeReserva, String sala, String cor, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        this.id = id;
        this.tituloAgenda = tituloAgenda;
        this.nomeReserva = nomeReserva;
        this.sala = sala;
        this.cor = cor;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
    }

    public Agenda(AgendaDto obj) {
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
        this.tituloAgenda = tituloAgenda;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return Objects.equals(id, agenda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
