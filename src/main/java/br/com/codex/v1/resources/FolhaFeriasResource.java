package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaFeriasDto;
import br.com.codex.v1.domain.dto.FolhaFeriasEventosDto;
import br.com.codex.v1.domain.rh.FolhaFerias;
import br.com.codex.v1.domain.rh.FolhaFeriasEventos;
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
@RequestMapping("v1/api/folha_ferias")
public class FolhaFeriasResource {

    @Autowired
    private FolhaFeriasCalculadaService folhaFeriasCalculadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaFeriasDto> create(@RequestBody FolhaFeriasDto folhaFeriasDto) {
        FolhaFerias obj = folhaFeriasCalculadaService.create(folhaFeriasDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping("/processar-lote")
    public ResponseEntity<List<FolhaFeriasDto>> processarLote(@RequestBody List<FolhaFeriasDto> folhasDto) {
        List<FolhaFerias> folhasProcessadas = folhaFeriasCalculadaService.processarLote(folhasDto);
        List<FolhaFeriasDto> listDto = folhasProcessadas.stream().map(FolhaFeriasDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaFerias> findById(@PathVariable Long id) {
        FolhaFerias obj = folhaFeriasCalculadaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaFeriasEventosDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaFeriasEventos> eventos = folhaFeriasCalculadaService.findAllEventosByFolhaFeriasId(id);
        List<FolhaFeriasEventosDto> listDto = eventos.stream().map(FolhaFeriasEventosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaFeriasDto>> findAll() {
        List<FolhaFerias> obj = folhaFeriasCalculadaService.findAll();
        List<FolhaFeriasDto> listObj = obj.stream().map(FolhaFeriasDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaFeriasCalculadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
