package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.TabelaNcm;
import br.com.codex.v1.domain.dto.TabelaNcmDto;
import br.com.codex.v1.service.TabelaNcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/tabela_ncm")
public class TabelaNcmResource {

    @Autowired
    private TabelaNcmService tabelaNcmService;


    @PostMapping
    public ResponseEntity<TabelaNcmDto> create(@Valid @RequestBody TabelaNcmDto tabelaNcmDto){
        TabelaNcm obj = tabelaNcmService.create(tabelaNcmDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{codigo}")
    public ResponseEntity<TabelaNcmDto> findByCodigo(@PathVariable String codigo){
        TabelaNcm objTabelaNcm = tabelaNcmService.findByCodigo(codigo);
        return ResponseEntity.ok().body(new TabelaNcmDto(objTabelaNcm));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TabelaNcmDto> findById(@PathVariable Integer id){
        TabelaNcm objTabelaNcm = tabelaNcmService.findById(id);
        return ResponseEntity.ok().body(new TabelaNcmDto(objTabelaNcm));
    }

    @GetMapping
    public ResponseEntity<List<TabelaNcmDto>> findAll(){
        List<TabelaNcm> objTabelaNcm = tabelaNcmService.finAll();
        List<TabelaNcmDto> tabelaNcmDto = objTabelaNcm.stream().map(TabelaNcmDto :: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(tabelaNcmDto);
    }
}
