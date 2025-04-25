package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.CadastroColaboradoresDto;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.service.CadastroColaboradoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/cadastro_colaboradores")
public class CadastroColaboradoresResource {

    @Autowired
    private CadastroColaboradoresService cadastroColaboradoresService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<CadastroColaboradoresDto> create (@Valid @RequestBody CadastroColaboradoresDto cadastroColaboradoresDto){
        CadastroColaboradores obj = cadastroColaboradoresService.create(cadastroColaboradoresDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CadastroColaboradoresDto> update (@PathVariable Integer id, @Valid @RequestBody CadastroColaboradoresDto cadastroColaboradoresDto){
        CadastroColaboradores obj = cadastroColaboradoresService.update(id, cadastroColaboradoresDto);
        return ResponseEntity.ok().body(new CadastroColaboradoresDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CadastroColaboradoresDto> delete(@PathVariable Integer id){
        cadastroColaboradoresService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CadastroColaboradoresDto> findById(@PathVariable Integer id){
        CadastroColaboradores objColaborador = cadastroColaboradoresService.findById(id);
        return ResponseEntity.ok().body(new CadastroColaboradoresDto(objColaborador));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/nome/{nomeColaborador}")
    public ResponseEntity<CadastroColaboradoresDto> findByNomeColaborador(@PathVariable String nomeColaborador){
        CadastroColaboradores objNomeColaborador = cadastroColaboradoresService.findByNomeColaborador(nomeColaborador);
        return ResponseEntity.ok().body(new CadastroColaboradoresDto(objNomeColaborador));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/situacao_colaboradores")
    public ResponseEntity<Map<String, Long>> findAllColaboradoresSituacao() {
        Map<String, Long> contagemSituacao = cadastroColaboradoresService.countBySituacaoAtual();
        return ResponseEntity.ok().body(contagemSituacao);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/situacao_contratados")
    public ResponseEntity<List<CadastroColaboradoresDto>> findAllColaboradoresAtivos() {
        List<CadastroColaboradores> list = cadastroColaboradoresService.findAllColaboradoresAtivos("Contratado");
        List<CadastroColaboradoresDto> listDto = list.stream().map(CadastroColaboradoresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/situacao_desligados")
    public ResponseEntity<List<CadastroColaboradoresDto>> findAllColaboradoresDemitidos(){
        List<CadastroColaboradores> list = cadastroColaboradoresService.findAllColaboradoresComSituacaoDesligado("Desligado");
        List<CadastroColaboradoresDto> listDto = list.stream().map(CadastroColaboradoresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
