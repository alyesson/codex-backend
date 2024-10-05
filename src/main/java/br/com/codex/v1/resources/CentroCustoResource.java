package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.CentroCusto;
import br.com.codex.v1.domain.dto.CentroCustoDto;
import br.com.codex.v1.service.CentroCustoService;
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
@RequestMapping(value = "v1/api/centro_custo")
public class CentroCustoResource {

    @Autowired
    private CentroCustoService centrocustoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PostMapping
    public ResponseEntity<CentroCustoDto> create(@Valid @RequestBody CentroCustoDto centrocustoDto){
        CentroCusto obj = centrocustoService.create(centrocustoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CentroCustoDto> update(@PathVariable Integer id, @Valid @RequestBody CentroCustoDto centrocustoDto){
        CentroCusto obj = centrocustoService.update(id, centrocustoDto);
        return ResponseEntity.ok().body(new CentroCustoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CentroCustoDto> delete(@PathVariable Integer id){
        centrocustoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CentroCustoDto> findById(@PathVariable Integer id){
        CentroCusto objGrupo = centrocustoService.findById(id);
        return ResponseEntity.ok().body(new CentroCustoDto(objGrupo));
    }

    @GetMapping
    public ResponseEntity<List<CentroCustoDto>> findAll(){
        List<CentroCusto> list = centrocustoService.findAll();
        List<CentroCustoDto> listDto = list.stream().map(CentroCustoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
