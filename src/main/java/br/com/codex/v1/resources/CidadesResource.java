package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.Cidades;
import br.com.codex.v1.domain.dto.CidadesDto;
import br.com.codex.v1.service.CidadesService;
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
@RequestMapping(value = "v1/api/cidades")
public class CidadesResource {

    @Autowired
    private CidadesService cidadesService;


    @PostMapping
    public ResponseEntity<CidadesDto> create(@Valid @RequestBody CidadesDto cidadesDto){
        Cidades obj = cidadesService.create(cidadesDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CidadesDto> findByCodigo(@PathVariable Integer codigo){
        Cidades objCidades = cidadesService.findByCodigo(codigo);
        return ResponseEntity.ok().body(new CidadesDto(objCidades));
    }

    @GetMapping
    public ResponseEntity<List<CidadesDto>> findAll(){
        List<Cidades> objCidades = cidadesService.finAll();
        List<CidadesDto> cidadesDto = objCidades.stream().map(CidadesDto :: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(cidadesDto);
    }
}
