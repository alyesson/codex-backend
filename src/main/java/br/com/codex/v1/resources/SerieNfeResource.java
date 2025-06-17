package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.SerieNfe;
import br.com.codex.v1.domain.dto.SerieNfeDto;
import br.com.codex.v1.service.SerieNfeService;
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
@RequestMapping("v1/api/serie_nfe")
public class SerieNfeResource {

    @Autowired
    private SerieNfeService serieNfeService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<SerieNfeDto> create(@Valid @RequestBody SerieNfeDto serienfeDto){
        SerieNfe obj = serieNfeService.create(serienfeDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SerieNfeDto> update(@PathVariable Long id, @Valid @RequestBody SerieNfeDto serienfeDto){
        SerieNfe obj = serieNfeService.update(id, serienfeDto);
        return ResponseEntity.ok().body(new SerieNfeDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SerieNfeDto> delete(@PathVariable Long id){
        serieNfeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<SerieNfeDto> findById(@PathVariable Long id){
        SerieNfe objGrupo = serieNfeService.findById(id);
        return ResponseEntity.ok().body(new SerieNfeDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'ESTOQUISTA')")
    @GetMapping
    public ResponseEntity<List<SerieNfeDto>> findAll(){
        List<SerieNfe> list = serieNfeService.findAll();
        List<SerieNfeDto> listDto = list.stream().map(SerieNfeDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
