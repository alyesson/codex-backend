package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaRescisaoDto;
import br.com.codex.v1.domain.dto.FolhaRescisaoEventosDto;
import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.domain.rh.FolhaRescisaoEventos;
import br.com.codex.v1.service.FolhaRescisaoCalculadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/folha_rescisao")
public class FolhaRescisaoResource {

    @Autowired
    private FolhaRescisaoCalculadaService folhaRescisaoCalculadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaRescisaoDto> create(@RequestBody FolhaRescisaoDto folhaRescisaoDto) {
        FolhaRescisao obj = folhaRescisaoCalculadaService.create(folhaRescisaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping("/processar-lote")
    public ResponseEntity<List<FolhaRescisaoDto>> processarLote(@RequestBody List<FolhaRescisaoDto> folhasDto) {
        List<FolhaRescisao> folhasProcessadas = folhaRescisaoCalculadaService.processarLote(folhasDto);
        List<FolhaRescisaoDto> listDto = folhasProcessadas.stream().map(FolhaRescisaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaRescisao> findById(@PathVariable Long id) {
        FolhaRescisao obj = folhaRescisaoCalculadaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaRescisaoEventosDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaRescisaoEventos> eventos = folhaRescisaoCalculadaService.findAllEventosByFolhaRescisaoId(id);
        List<FolhaRescisaoEventosDto> listDto = eventos.stream().map(FolhaRescisaoEventosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaRescisaoDto>> findAll() {
        List<FolhaRescisao> obj = folhaRescisaoCalculadaService.findAll();
        List<FolhaRescisaoDto> listObj = obj.stream().map(FolhaRescisaoDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaRescisaoCalculadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}