package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.AfastamentoDto;
import br.com.codex.v1.domain.rh.Afastamento;
import br.com.codex.v1.service.AfastamentoService;
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
@RequestMapping(value = "v1/api/afastamento")
public class AfastamentoResource {

    @Autowired
    private AfastamentoService afastamentoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<AfastamentoDto> create(@Valid @RequestBody AfastamentoDto afastamentoDto){
        Afastamento obj = afastamentoService.create(afastamentoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<AfastamentoDto>> findAll(){
        List<Afastamento> list = afastamentoService.findAll();
        List<AfastamentoDto> listDto = list.stream().map(AfastamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AfastamentoDto> update(@PathVariable Long id, @Valid @RequestBody AfastamentoDto afastamentoDto){
        Afastamento obj = afastamentoService.update(id, afastamentoDto);
        return ResponseEntity.ok().body(new AfastamentoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AfastamentoDto> delete(@PathVariable Long id){
        afastamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AfastamentoDto> findById(@PathVariable Long id){
        Afastamento objGrupo = afastamentoService.findById(id);
        return ResponseEntity.ok().body(new AfastamentoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH', 'RH')")
    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<AfastamentoDto> findByCpf(@PathVariable String cpf){
        Afastamento obj = afastamentoService.findByCpf(cpf);
        return ResponseEntity.ok().body(new AfastamentoDto(obj));
    }
}
