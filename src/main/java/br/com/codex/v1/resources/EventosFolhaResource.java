package br.com.codex.v1.resources;


import br.com.codex.v1.domain.dto.EventosFolhaDto;
import br.com.codex.v1.domain.rh.EventosFolha;
import br.com.codex.v1.service.EventosFolhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/eventos_folha")
public class EventosFolhaResource {

    @Autowired
    private EventosFolhaService eventosFolhaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<EventosFolhaDto> create(@Valid @RequestBody EventosFolhaDto eventosFolhaDto){
        EventosFolha obj = eventosFolhaService.create(eventosFolhaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<EventosFolhaDto> update(@PathVariable Long id, @Valid @RequestBody EventosFolhaDto eventosFolhaDto){
        EventosFolha obj = eventosFolhaService.update(id, eventosFolhaDto);
        return ResponseEntity.ok().body(new EventosFolhaDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EventosFolhaDto> delete(@PathVariable Long id){
        eventosFolhaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventosFolhaDto> findById(@PathVariable Long id){
        EventosFolha objGrupo = eventosFolhaService.findById(id);
        return ResponseEntity.ok().body(new EventosFolhaDto(objGrupo));
    }

    @GetMapping
    public ResponseEntity<List<EventosFolhaDto>> findAll(){
        List<EventosFolha> list = eventosFolhaService.findAll();
        List<EventosFolhaDto> listDto = list.stream().map(EventosFolhaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
