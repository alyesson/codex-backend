package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Agenda;
import br.com.codex.v1.domain.dto.AgendaDto;
import br.com.codex.v1.domain.repository.AgendaRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;


    public Agenda create(AgendaDto agendaDto) {
        agendaDto.setId(null);
        Agenda agenda = new Agenda(agendaDto);
        return agendaRepository.save(agenda);
    }

    public Agenda update(Integer id, AgendaDto agendaDto) {
        agendaDto.setId(id);
        Agenda obj = findById(id);
        obj = new Agenda(agendaDto);
        return agendaRepository.save(obj);
    }

    public Agenda findById(Integer id) {
        Optional<Agenda> obj = agendaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Agenda não encontrada"));
    }

    public void delete(String nomeReserva, Integer id) {
        agendaRepository.deleteByNomeReservaAndId(nomeReserva, id);
    }

    public List<Agenda> findAll(){
        return agendaRepository.findAll();
    }
}
