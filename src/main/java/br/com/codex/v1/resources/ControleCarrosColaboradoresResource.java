package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.ControleCarrosColaboradores;
import br.com.codex.v1.domain.dto.ControleCarrosColaboradoresDto;
import br.com.codex.v1.service.ControleCarrosColaboradoresService;
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
@RequestMapping(value = "v1/api/controle_veiculos_colaboradores")
public class ControleCarrosColaboradoresResource {

    @Autowired
    private ControleCarrosColaboradoresService controleCarrosColaboradoresService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO')")
    @PostMapping
    public ResponseEntity<ControleCarrosColaboradoresDto> create(@Valid @RequestBody ControleCarrosColaboradoresDto controleCarrosColaboradoresDto){
        ControleCarrosColaboradores obj = controleCarrosColaboradoresService.create(controleCarrosColaboradoresDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ControleCarrosColaboradoresDto> update(@PathVariable Integer id, @Valid @RequestBody ControleCarrosColaboradoresDto controleCarrosColaboradoresDto){
        ControleCarrosColaboradores obj = controleCarrosColaboradoresService.update(id, controleCarrosColaboradoresDto);
        return ResponseEntity.ok().body(new ControleCarrosColaboradoresDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO','GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ControleCarrosColaboradoresDto> delete(@PathVariable Integer id){
        controleCarrosColaboradoresService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ControleCarrosColaboradoresDto> findById(@PathVariable Integer id){
        ControleCarrosColaboradores objGrupo = controleCarrosColaboradoresService.findById(id);
        return ResponseEntity.ok().body(new ControleCarrosColaboradoresDto(objGrupo));
    }

    @GetMapping(value = "/placa_veiculo/{placa_veiculo}")
    public ResponseEntity<ControleCarrosColaboradoresDto> findByPlacaVeiculo(@PathVariable String placaVeiculo){
        ControleCarrosColaboradores objPlaca = controleCarrosColaboradoresService.findByPlacaVeiculo(placaVeiculo);
        return ResponseEntity.ok().body(new ControleCarrosColaboradoresDto(objPlaca));
    }

    @GetMapping
    public ResponseEntity<List<ControleCarrosColaboradoresDto>> findAll(){
        List<ControleCarrosColaboradores> list = controleCarrosColaboradoresService.findAll();
        List<ControleCarrosColaboradoresDto> listDto = list.stream().map(ControleCarrosColaboradoresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
