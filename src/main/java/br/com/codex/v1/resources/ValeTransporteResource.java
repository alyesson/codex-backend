package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ValeTransporteDto;
import br.com.codex.v1.domain.rh.ValeTransporte;
import br.com.codex.v1.service.ValeTransporteService;
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
@RequestMapping("v1/api/vale_transporte")
public class ValeTransporteResource {

    @Autowired
    private ValeTransporteService service;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<ValeTransporteDto> create(@Valid @RequestBody ValeTransporteDto valeTransporteDto) {
        ValeTransporte obj = service.create(valeTransporteDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping("/{id}")
    public ResponseEntity<ValeTransporteDto> update(@PathVariable Long id, @Valid @RequestBody ValeTransporteDto valeTransporteDto) {
        ValeTransporte obj = service.update(id, valeTransporteDto);
        return ResponseEntity.ok().body(new ValeTransporteDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ValeTransporteDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ValeTransporteDto> findById(@PathVariable Long id) {
        ValeTransporte obj = service.findById(id);
        return ResponseEntity.ok().body(new ValeTransporteDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping
    public ResponseEntity<List<ValeTransporteDto>> findAll() {
        List<ValeTransporte> list = service.findAll();
        List<ValeTransporteDto> listDto = list.stream().map(ValeTransporteDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<ValeTransporteDto>> findByColaboradorId(@PathVariable Long colaboradorId) {
        List<ValeTransporte> list = service.findByColaboradorId(colaboradorId);
        List<ValeTransporteDto> listDto = list.stream().map(ValeTransporteDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/ativos")
    public ResponseEntity<List<ValeTransporteDto>> findAtivos() {
        List<ValeTransporte> list = service.findAtivos();
        List<ValeTransporteDto> listDto = list.stream().map(ValeTransporteDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/colaborador/cpf/{cpf}")
    public ResponseEntity<ValeTransporteDto> findAtivoByColaboradorCpf(@PathVariable String cpf) {
        ValeTransporte obj = service.findAtivoByColaboradorCpf(cpf);
        return ResponseEntity.ok().body(new ValeTransporteDto(obj));
    }
}
