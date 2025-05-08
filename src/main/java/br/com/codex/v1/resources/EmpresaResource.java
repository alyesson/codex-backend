package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/empresas")
public class EmpresaResource {

    @Autowired
    private EmpresaService empresaService;

    public ResponseEntity<EmpresaDto> create(@Valid @RequestBody EmpresaDto empresaDto){
        Empresa obj = empresaService.create(empresaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDto>> findAll(){
        List<Empresa> list = empresaService.findAll();
        List<EmpresaDto> listDto = list.stream().map(EmpresaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
