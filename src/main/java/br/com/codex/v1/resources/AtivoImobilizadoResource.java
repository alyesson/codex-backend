package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import br.com.codex.v1.domain.dto.AtivoImobilizadoDto;
import br.com.codex.v1.service.AtivoImobilizadoService;
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
@RequestMapping(value = "v1/api/ativo_imobilizado")
public class AtivoImobilizadoResource {

    @Autowired
    private AtivoImobilizadoService ativoImobilizadoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<AtivoImobilizadoDto> create(@Valid @RequestBody AtivoImobilizadoDto ativoImobilizadoDto){
        AtivoImobilizado obj = ativoImobilizadoService.create(ativoImobilizadoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<AtivoImobilizadoDto>> findAll(){
        List<AtivoImobilizado> list = ativoImobilizadoService.findAll();
        List<AtivoImobilizadoDto> listDto = list.stream().map(AtivoImobilizadoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AtivoImobilizadoDto> update(@PathVariable Long id, @Valid @RequestBody AtivoImobilizadoDto ativoImobilizadoDto){
        AtivoImobilizado obj = ativoImobilizadoService.update(id, ativoImobilizadoDto);
        return ResponseEntity.ok().body(new AtivoImobilizadoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AtivoImobilizadoDto> delete(@PathVariable Long id){
        ativoImobilizadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AtivoImobilizadoDto> findById(@PathVariable Long id){
        AtivoImobilizado obj = ativoImobilizadoService.findById(id);
        return ResponseEntity.ok().body(new AtivoImobilizadoDto(obj));
    }
}
