package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.SalaReuniao;
import br.com.codex.v1.domain.dto.SalaReuniaoDto;
import br.com.codex.v1.service.SalaReuniaoService;
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
@RequestMapping("v1/api/sala_reuniao")
public class SalaReuniaoResource {

    @Autowired
    private SalaReuniaoService salaReuniaoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PostMapping
    public ResponseEntity<SalaReuniaoDto> create(@Valid @RequestBody SalaReuniaoDto salaReuniaoDto){
        SalaReuniao objSalaReuniao = salaReuniaoService.create(salaReuniaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objSalaReuniao.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SalaReuniaoDto> update(@PathVariable Long id, @Valid @RequestBody SalaReuniaoDto salaReuniaoDto){
        SalaReuniao objSalaReuniao = salaReuniaoService.update(id, salaReuniaoDto);
        return ResponseEntity.ok().body(new SalaReuniaoDto((objSalaReuniao)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SalaReuniaoDto> delete(@PathVariable Long id){
        salaReuniaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SalaReuniaoDto> findById(@PathVariable Long id){
        SalaReuniao objSalaReuniao = salaReuniaoService.findById(id);
        return ResponseEntity.ok().body(new SalaReuniaoDto(objSalaReuniao));
    }

    @GetMapping
    public ResponseEntity<List<SalaReuniaoDto>> findAll(){
        List<SalaReuniao> list = salaReuniaoService.findAll();
        List<SalaReuniaoDto> listDto = list.stream().map(SalaReuniaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
