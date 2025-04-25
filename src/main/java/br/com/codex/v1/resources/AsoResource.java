package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.AsoDto;
import br.com.codex.v1.domain.rh.Aso;
import br.com.codex.v1.service.AsoService;
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
@RequestMapping("v1/api/aso")
public class AsoResource {

    @Autowired
    private AsoService asoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<AsoDto> create(@Valid @RequestBody AsoDto asoDto){
        Aso objAso = asoService.create(asoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objAso.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AsoDto> update(@PathVariable Integer id, @Valid @RequestBody AsoDto asoDto){
        Aso objAso = asoService.update(id, asoDto);
        return ResponseEntity.ok().body(new AsoDto((objAso)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AsoDto> delete(@PathVariable Integer id){
        asoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AsoDto> findById(@PathVariable Integer id){
        Aso objAso = asoService.findById(id);
        return ResponseEntity.ok().body(new AsoDto(objAso));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<AsoDto>> findAll(){
        List<Aso> list = asoService.findAll();
        List<AsoDto> listDto = list.stream().map(AsoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
