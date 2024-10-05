package br.com.codex.v1.resources;

import br.com.codex.v1.domain.estoque.SubGrupo;
import br.com.codex.v1.domain.dto.SubGrupoDto;
import br.com.codex.v1.service.SubGrupoService;
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
@RequestMapping("v1/api/sub_grupos")
public class SubGrupoResource {
    @Autowired
    private SubGrupoService subgrupoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @PostMapping
    public ResponseEntity<SubGrupoDto> create(@Valid @RequestBody SubGrupoDto subgrupoDto){
        SubGrupo obj = subgrupoService.create(subgrupoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SubGrupoDto> update(@PathVariable Integer id, @Valid @RequestBody SubGrupoDto subgrupoDto){
        SubGrupo obj = subgrupoService.update(id, subgrupoDto);
        return ResponseEntity.ok().body(new SubGrupoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SubGrupoDto> delete(@PathVariable Integer id){
        subgrupoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SubGrupoDto> findById(@PathVariable Integer id){
        SubGrupo objGrupo = subgrupoService.findById(id);
        return ResponseEntity.ok().body(new SubGrupoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @GetMapping
    public ResponseEntity<List<SubGrupoDto>> findAll(){
        List<SubGrupo> list = subgrupoService.findAll();
        List<SubGrupoDto> listDto = list.stream().map(SubGrupoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
