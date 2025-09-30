package br.com.codex.v1.resources;


import br.com.codex.v1.domain.dto.CadastroJornadaDto;
import br.com.codex.v1.domain.rh.CadastroJornada;
import br.com.codex.v1.service.CadastroJornadaService;
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
@RequestMapping(value = "v1/api/jornadas")
public class CadastroJornadaResource {

    @Autowired
    private CadastroJornadaService cadastroJornadaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<CadastroJornadaDto> create(@Valid @RequestBody CadastroJornadaDto cadastroJornadaDto){
        CadastroJornada obj = cadastroJornadaService.create(cadastroJornadaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/codigo_jornada")
    public ResponseEntity<List<CadastroJornadaDto>> findByNumeroNotaFiscal(@RequestParam(value = "codigoJornada") String codigoJornada){
        List<CadastroJornada> list = cadastroJornadaService.findAllByCodigoJornada(codigoJornada);
        List<CadastroJornadaDto> objList = list.stream().map(CadastroJornadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objList);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CadastroJornadaDto> update(@PathVariable Long id, @Valid @RequestBody CadastroJornadaDto cadastroJornadaDto){
        CadastroJornada obj = cadastroJornadaService.update(id, cadastroJornadaDto);
        return ResponseEntity.ok().body(new CadastroJornadaDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CadastroJornadaDto> delete(@PathVariable Long id){
        cadastroJornadaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CadastroJornadaDto> findById(@PathVariable Long id){
        CadastroJornada objJornadas = cadastroJornadaService.findById(id);
        return ResponseEntity.ok().body(new CadastroJornadaDto(objJornadas));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<CadastroJornadaDto>> findAll(){
        List<CadastroJornada> list = cadastroJornadaService.findAll();
        List<CadastroJornadaDto> listDto = list.stream().map(CadastroJornadaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
