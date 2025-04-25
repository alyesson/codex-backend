package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.Agenda;
import br.com.codex.v1.domain.dto.AgendaDto;
import br.com.codex.v1.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/agenda")
public class AgendaResource {

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<AgendaDto> create(@Valid @RequestBody AgendaDto agendaDto){
        Agenda objAgenda = agendaService.create(agendaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objAgenda.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AgendaDto> update(@PathVariable Integer id, @Valid @RequestBody AgendaDto agendaDto){
        Agenda objAgenda = agendaService.update(id, agendaDto);
        return ResponseEntity.ok().body(new AgendaDto((objAgenda)));
    }

    @DeleteMapping(value = "/remove_sala")
    public ResponseEntity<AgendaDto> delete(@RequestParam("nomeReserva") String nomeReserva, @RequestParam("id") Integer id){
        agendaService.delete(nomeReserva, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AgendaDto> findById(@PathVariable Integer id){
        Agenda objAgenda = agendaService.findById(id);
        return ResponseEntity.ok().body(new AgendaDto(objAgenda));
    }

    @GetMapping
    public ResponseEntity<List<AgendaDto>> findAll(){
        List<Agenda> list = agendaService.findAll();
        List<AgendaDto> listDto = list.stream().map(AgendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
