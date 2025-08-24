package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ServicosDto;
import br.com.codex.v1.domain.vendas.Servicos;
import br.com.codex.v1.service.ServicosService;
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
@RequestMapping("v1/api/servicos")
public class ServicosResource {

    @Autowired
    private ServicosService servicosService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<ServicosDto>> findAll(){
        List<Servicos> list = servicosService.findAll();
        List<ServicosDto> listDto = list.stream().map(ServicosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<ServicosDto> create(@Valid @RequestBody ServicosDto servicosDto){
        Servicos objServicos = servicosService.create(servicosDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objServicos.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ServicosDto> update(@PathVariable Long id, @Valid @RequestBody ServicosDto servicosDto){
        Servicos objServicos = servicosService.update(id, servicosDto);
        return ResponseEntity.ok().body(new ServicosDto((objServicos)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ServicosDto> delete(@PathVariable Long id){
        servicosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ServicosDto> findById(@PathVariable Long id){
        Servicos objServicos = servicosService.findById(id);
        return ResponseEntity.ok().body(new ServicosDto(objServicos));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/codigo/{codigo}")
    public ResponseEntity<ServicosDto> findByCodigo(@PathVariable String codigo){
        Servicos objServicos = servicosService.findByCodigo(codigo);
        return ResponseEntity.ok().body(new ServicosDto(objServicos));
    }
}
