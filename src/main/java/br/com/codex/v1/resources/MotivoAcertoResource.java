package br.com.codex.v1.resources;

import br.com.codex.v1.domain.estoque.MotivoAcerto;
import br.com.codex.v1.domain.dto.MotivoAcertoDto;
import br.com.codex.v1.service.MotivoAcertoService;
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
@RequestMapping(value = "v1/api/motivo_acerto")
public class MotivoAcertoResource {

    @Autowired
    private MotivoAcertoService motivoAcertoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI', 'GERENTE_ESTOQUE')")
    @PostMapping
    public ResponseEntity<MotivoAcertoDto> create(@Valid @RequestBody MotivoAcertoDto motivoacertoDto){
        MotivoAcerto objMotivoAcerto = motivoAcertoService.create(motivoacertoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objMotivoAcerto.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI','GERENTE_ESTOQUE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<MotivoAcertoDto> update(@PathVariable Long id, @Valid @RequestBody MotivoAcertoDto motivoacertoDto){
        MotivoAcerto objMotivoAcerto = motivoAcertoService.update(id, motivoacertoDto);
        return ResponseEntity.ok().body(new MotivoAcertoDto((objMotivoAcerto)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI','GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MotivoAcertoDto> delete(@PathVariable Long id){
        motivoAcertoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<MotivoAcertoDto> findById(@PathVariable Long id){
        MotivoAcerto objMotivoAcerto = motivoAcertoService.findById(id);
        return ResponseEntity.ok().body(new MotivoAcertoDto(objMotivoAcerto));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'GERENTE', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @GetMapping
    public ResponseEntity<List<MotivoAcertoDto>> findAll(){
        List<MotivoAcerto> list = motivoAcertoService.findAll();
        List<MotivoAcertoDto> listDto = list.stream().map(MotivoAcertoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
