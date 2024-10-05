package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.Departamento;
import br.com.codex.v1.domain.dto.DepartamentoDto;
import br.com.codex.v1.service.exceptions.DepartamentoService;
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
@RequestMapping(value = "v1/api/departamento")
public class DepartamentoResource {

    @Autowired
    private DepartamentoService departamentoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PostMapping
    public ResponseEntity<DepartamentoDto> create(@Valid @RequestBody DepartamentoDto departamentoDto){
        Departamento obj = departamentoService.create(departamentoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<DepartamentoDto> update(@PathVariable Integer id, @Valid @RequestBody DepartamentoDto departamentoDto){
        Departamento obj = departamentoService.update(id, departamentoDto);
        return ResponseEntity.ok().body(new DepartamentoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<DepartamentoDto> delete(@PathVariable Integer id){
        departamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DepartamentoDto> findById(@PathVariable Integer id){
        Departamento objGrupo = departamentoService.findById(id);
        return ResponseEntity.ok().body(new DepartamentoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @GetMapping
    public ResponseEntity<List<DepartamentoDto>> findAll(){
        List<Departamento> list = departamentoService.findAll();
        List<DepartamentoDto> listDto = list.stream().map(DepartamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
