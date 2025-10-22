package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaMensalDto;
import br.com.codex.v1.domain.dto.FolhaMensalEventosDto;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaMensalEventos;
import br.com.codex.v1.service.FolhaMensalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/folha_mensal")
public class FolhaMensalResource {

    @Autowired
    private FolhaMensalService folhaMensalService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaMensalDto> create(@RequestBody FolhaMensalDto folhaMensalDto) {
        FolhaMensal obj = folhaMensalService.create(folhaMensalDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping("/{id}")
    public ResponseEntity<FolhaMensalDto> update(@PathVariable Long id, @RequestBody FolhaMensalDto folhaMensalDto) {
        FolhaMensal obj = folhaMensalService.update(id, folhaMensalDto);
        return ResponseEntity.ok().body(new FolhaMensalDto((obj)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaMensal> findById(@PathVariable Long id) {
        FolhaMensal obj = folhaMensalService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaMensalEventosDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaMensalEventos> eventos = folhaMensalService.findAllEventosByFolhaMensalId(id);
        List<FolhaMensalEventosDto> listDto = eventos.stream().map(FolhaMensalEventosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaMensalDto>> findAll() {
        List<FolhaMensal> obj = folhaMensalService.findAll();
        List<FolhaMensalDto> listObj = obj.stream().map(FolhaMensalDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaMensalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
