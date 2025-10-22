package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaMensalCalculadaDto;
import br.com.codex.v1.domain.dto.FolhaMensalEventosCalculadaDto;
import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
import br.com.codex.v1.domain.rh.FolhaMensalEventosCalculada;
import br.com.codex.v1.service.FolhaMensalCalculadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/folha_mensal_calculada")
public class FolhaMensalCalculadaResource {

    @Autowired
    private FolhaMensalCalculadaService folhaMensalCalculadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaMensalCalculadaDto> create(@RequestBody FolhaMensalCalculadaDto folhaMensalCalculadaDto) {
        FolhaMensalCalculada obj = folhaMensalCalculadaService.create(folhaMensalCalculadaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping("/processar-lote")
    public ResponseEntity<List<FolhaMensalCalculadaDto>> processarLote(@RequestBody List<FolhaMensalCalculadaDto> folhasDto) {
        List<FolhaMensalCalculada> folhasProcessadas = folhaMensalCalculadaService.processarLote(folhasDto);
        List<FolhaMensalCalculadaDto> listDto = folhasProcessadas.stream().map(FolhaMensalCalculadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaMensalCalculada> findById(@PathVariable Long id) {
        FolhaMensalCalculada obj = folhaMensalCalculadaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaMensalEventosCalculadaDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaMensalEventosCalculada> eventos = folhaMensalCalculadaService.findAllEventosByFolhaMensalCalculadaId(id);
        List<FolhaMensalEventosCalculadaDto> listDto = eventos.stream().map(FolhaMensalEventosCalculadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaMensalCalculadaDto>> findAll() {
        List<FolhaMensalCalculada> obj = folhaMensalCalculadaService.findAll();
        List<FolhaMensalCalculadaDto> listObj = obj.stream().map(FolhaMensalCalculadaDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaMensalCalculadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
