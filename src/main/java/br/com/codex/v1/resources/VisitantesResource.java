package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.Visitantes;
import br.com.codex.v1.domain.dto.VisitantesDto;
import br.com.codex.v1.service.VisitantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/visitantes")
public class VisitantesResource {

    @Autowired
    private VisitantesService visitantesService;

    @PostMapping
    public ResponseEntity<VisitantesDto> create(@Valid @RequestBody VisitantesDto visitantesDto){
        Visitantes obj = visitantesService.create(visitantesDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<VisitantesDto> delete(@PathVariable Integer id){
        visitantesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VisitantesDto> findById(@PathVariable Integer id){
        Visitantes objVisitante = visitantesService.findById(id);
        return ResponseEntity.ok().body(new VisitantesDto(objVisitante));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @GetMapping(value = "/visitantes_periodo")
    public ResponseEntity<List<VisitantesDto>> findAllAtendimentosPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Visitantes> list = visitantesService.findAllVisitantesPeriodo(dataInicial, dataFinal);
        List<VisitantesDto> listDto = list.stream().map(VisitantesDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
