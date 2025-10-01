package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.CadastroCargosDto;
import br.com.codex.v1.domain.rh.CadastroCargos;
import br.com.codex.v1.service.CadastroCargosService;
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
@RequestMapping(value = "v1/api/cargos")
public class CadastroCargosResource {

    @Autowired
    private CadastroCargosService cadastroCargosService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<CadastroCargosDto> create(@Valid @RequestBody CadastroCargosDto cadastroCargosDto){
        CadastroCargos obj = cadastroCargosService.create(cadastroCargosDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CadastroCargosDto> update(@PathVariable Long id, @Valid @RequestBody CadastroCargosDto cadastroCargosDto){
        CadastroCargos obj = cadastroCargosService.update(id, cadastroCargosDto);
        return ResponseEntity.ok().body(new CadastroCargosDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CadastroCargosDto> delete(@PathVariable Long id){
        cadastroCargosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CadastroCargosDto> findById(@PathVariable Long id){
        CadastroCargos objGrupo = cadastroCargosService.findById(id);
        return ResponseEntity.ok().body(new CadastroCargosDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<CadastroCargosDto>> findAll(){
        List<CadastroCargos> list = cadastroCargosService.findAll();
        List<CadastroCargosDto> listDto = list.stream().map(CadastroCargosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
