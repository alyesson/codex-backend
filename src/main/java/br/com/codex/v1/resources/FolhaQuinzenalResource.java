package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.FolhaQuinzenalDto;
import br.com.codex.v1.domain.dto.FolhaQuinzenalEventosDto;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.domain.rh.FolhaQuinzenalEventos;
import br.com.codex.v1.service.FolhaQuinzenalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/cadastro_folha_quinzenal")
public class FolhaQuinzenalResource {

    @Autowired
    private FolhaQuinzenalService folhaQuinzenalService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<FolhaQuinzenalDto> create(@RequestBody FolhaQuinzenalDto folhaQuinzenalDto) {
        FolhaQuinzenal obj = folhaQuinzenalService.create(folhaQuinzenalDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping
    public ResponseEntity<FolhaQuinzenalDto> update(@PathVariable Long id,
                                                    @RequestBody FolhaQuinzenalDto folhaQuinzenalDto) {
        FolhaQuinzenal obj = folhaQuinzenalService.update(id, folhaQuinzenalDto);
        return ResponseEntity.ok().body(new FolhaQuinzenalDto((obj)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaQuinzenal> findById(@PathVariable Long id) {
        FolhaQuinzenal obj = folhaQuinzenalService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<FolhaQuinzenalEventosDto>> findEventosByFolhaId(@PathVariable Long id) {
        List<FolhaQuinzenalEventos> eventos = folhaQuinzenalService.findAllEventosByCadastroFolhaPagamentoQuinzenalId(id);
        List<FolhaQuinzenalEventosDto> listDto = eventos.stream().map(FolhaQuinzenalEventosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<FolhaQuinzenalDto>> findAll() {
        List<FolhaQuinzenal> obj = folhaQuinzenalService.findAll();
        List<FolhaQuinzenalDto> listObj = obj.stream().map(FolhaQuinzenalDto:: new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        folhaQuinzenalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
