package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.CalculoFeriasDto;
import br.com.codex.v1.domain.dto.CalculoFeriasEventosDto;
import br.com.codex.v1.domain.rh.CalculoFerias;
import br.com.codex.v1.domain.rh.CalculoFeriasEventos;
import br.com.codex.v1.service.FolhaFeriasCalculadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/calculo_ferias")
public class CalculoFeriasResource {

    @Autowired
    private FolhaFeriasCalculadaService folhaFeriasCalculadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<CalculoFeriasDto> create(@RequestBody CalculoFeriasDto calculoFeriasDto) {
        CalculoFerias obj = folhaFeriasCalculadaService.create(calculoFeriasDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping("/processar-lote")
    public ResponseEntity<List<CalculoFeriasDto>> processarLote(@RequestBody List<CalculoFeriasDto> folhasDto) {
        List<CalculoFerias> folhasProcessadas = folhaFeriasCalculadaService.processarLote(folhasDto);
        List<CalculoFeriasDto> listDto = folhasProcessadas.stream().map(CalculoFeriasDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalculoFerias> findById(@PathVariable Long id) {
        CalculoFerias obj = folhaFeriasCalculadaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<CalculoFeriasEventosDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<CalculoFeriasEventos> eventos = folhaFeriasCalculadaService.findAllEventosByCalculoFeriasId(id);
        List<CalculoFeriasEventosDto> listDto = eventos.stream().map(CalculoFeriasEventosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<CalculoFeriasDto>> findAll() {
        List<CalculoFerias> obj = folhaFeriasCalculadaService.findAll();
        List<CalculoFeriasDto> listObj = obj.stream().map(CalculoFeriasDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaFeriasCalculadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
