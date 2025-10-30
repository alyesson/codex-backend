package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaQuinzenalCalculadaDto;
import br.com.codex.v1.domain.dto.FolhaQuinzenalEventosCalculadaDto;
import br.com.codex.v1.domain.rh.FolhaQuinzenalCalculada;
import br.com.codex.v1.domain.rh.FolhaQuinzenalEventosCalculada;
import br.com.codex.v1.service.FolhaQuinzenalCalculadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/folha_quinzenal_calculada")
public class FolhaQuinzenalCalculadaResource {

    @Autowired
    private FolhaQuinzenalCalculadaService folhaQuinzenalCalculadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaQuinzenalCalculadaDto> create(@RequestBody FolhaQuinzenalCalculadaDto folhaQuinzenalCalculadaDto) {
        FolhaQuinzenalCalculada obj = folhaQuinzenalCalculadaService.create(folhaQuinzenalCalculadaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping("/processar-lote")
    public ResponseEntity<List<FolhaQuinzenalCalculadaDto>> processarLote(@RequestBody List<FolhaQuinzenalCalculadaDto> folhasDto) {
        List<FolhaQuinzenalCalculada> folhasProcessadas = folhaQuinzenalCalculadaService.processarLote(folhasDto);
        List<FolhaQuinzenalCalculadaDto> listDto = folhasProcessadas.stream().map(FolhaQuinzenalCalculadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaQuinzenalCalculada> findById(@PathVariable Long id) {
        FolhaQuinzenalCalculada obj = folhaQuinzenalCalculadaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaQuinzenalEventosCalculadaDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaQuinzenalEventosCalculada> eventos = folhaQuinzenalCalculadaService.findAllEventosByFolhaQuinzenalCalculadaId(id);
        List<FolhaQuinzenalEventosCalculadaDto> listDto = eventos.stream().map(FolhaQuinzenalEventosCalculadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaQuinzenalCalculadaDto>> findAll() {
        List<FolhaQuinzenalCalculada> obj = folhaQuinzenalCalculadaService.findAll();
        List<FolhaQuinzenalCalculadaDto> listObj = obj.stream().map(FolhaQuinzenalCalculadaDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaQuinzenalCalculadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}