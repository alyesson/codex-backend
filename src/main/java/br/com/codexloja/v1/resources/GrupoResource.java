package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.estoque.Grupo;
import br.com.codexloja.v1.domain.dto.GrupoDto;
import br.com.codexloja.v1.service.GrupoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/grupos")
public class GrupoResource {

    @Autowired
    private GrupoService grupoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PostMapping
    public ResponseEntity<GrupoDto> create(@Valid @RequestBody GrupoDto grupoDto){
        Grupo objGrupo = grupoService.create(grupoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objGrupo.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<GrupoDto> update(@PathVariable Integer id, @Valid @RequestBody GrupoDto grupoDto){
        Grupo objGrupo = grupoService.update(id, grupoDto);
        return ResponseEntity.ok().body(new GrupoDto((objGrupo)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<GrupoDto> delete(@PathVariable Integer id){
        grupoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<GrupoDto> findById(@PathVariable Integer id){
        Grupo objGrupo = grupoService.findById(id);
        return ResponseEntity.ok().body(new GrupoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @GetMapping
    public ResponseEntity<List<GrupoDto>> findAll(){
        List<Grupo> list = grupoService.findAll();
        List<GrupoDto> listDto = list.stream().map(GrupoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
