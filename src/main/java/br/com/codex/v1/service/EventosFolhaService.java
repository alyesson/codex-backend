package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Cidades;
import br.com.codex.v1.domain.dto.CentroCustoDto;
import br.com.codex.v1.domain.dto.EventosFolhaDto;
import br.com.codex.v1.domain.financeiro.CentroCusto;
import br.com.codex.v1.domain.repository.EventosFolhaRepository;
import br.com.codex.v1.domain.rh.EventosFolha;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventosFolhaService {

    @Autowired
    private EventosFolhaRepository eventosFolhaRepository;

    public EventosFolha create (EventosFolhaDto eventosFolhaDto){

        eventosFolhaDto.setId(null);
        EventosFolha objEvento = new EventosFolha(eventosFolhaDto);
        return eventosFolhaRepository.save(objEvento);
    }

    public EventosFolha update(Long id, EventosFolhaDto eventosFolhaDto) {
        eventosFolhaDto.setId(id);
        EventosFolha obj = findById(id);
        obj = new EventosFolha(eventosFolhaDto);
        return eventosFolhaRepository.save(obj);
    }

    public EventosFolha findById(Long id) {
        Optional<EventosFolha> obj = eventosFolhaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Evento não encontrado"));
    }

    public List<EventosFolha> findAll() {
        return eventosFolhaRepository.findAll();
    }

    public void delete(Long id){
        eventosFolhaRepository.deleteById(id);
    }
}
