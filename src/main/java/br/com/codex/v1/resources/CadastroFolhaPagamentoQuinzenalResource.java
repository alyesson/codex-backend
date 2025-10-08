package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.CadastroFolhaPagamentoQuinzenalDto;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenal;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenalEventos;
import br.com.codex.v1.service.CadastroFolhaPagamentoQuinzenalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("v1/api/cadastro_folha_quinzenal")
public class CadastroFolhaPagamentoQuinzenalResource {

    @Autowired
    private CadastroFolhaPagamentoQuinzenalService cadastroFolhaPagamentoQuinzenalService;

    @PostMapping
    public ResponseEntity<CadastroFolhaPagamentoQuinzenal> create(
            @RequestBody CadastroFolhaPagamentoQuinzenalDto cadastroFolhaPagamentoQuinzenalDto) {
        CadastroFolhaPagamentoQuinzenal folhaPagamentoQuinzenal = cadastroFolhaPagamentoQuinzenalService.create(cadastroFolhaPagamentoQuinzenalDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(folhaPagamentoQuinzenal.getId())
                .toUri();
        return ResponseEntity.created(uri).body(folhaPagamentoQuinzenal);
    }

    @PutMapping
    public ResponseEntity<CadastroFolhaPagamentoQuinzenal> update(
            @RequestBody CadastroFolhaPagamentoQuinzenalDto cadastroFolhaPagamentoQuinzenalDto) {
        CadastroFolhaPagamentoQuinzenal folhaPagamentoQuinzenal = cadastroFolhaPagamentoQuinzenalService.update(cadastroFolhaPagamentoQuinzenalDto);
        return ResponseEntity.ok().body(folhaPagamentoQuinzenal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CadastroFolhaPagamentoQuinzenal> findById(@PathVariable Long id) {
        CadastroFolhaPagamentoQuinzenal folhaPagamentoQuinzenal = cadastroFolhaPagamentoQuinzenalService.findById(id);
        return ResponseEntity.ok().body(folhaPagamentoQuinzenal);
    }

    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<CadastroFolhaPagamentoQuinzenalEventos>> findEventosByFolhaId(@PathVariable Long id) {
        List<CadastroFolhaPagamentoQuinzenalEventos> eventos = cadastroFolhaPagamentoQuinzenalService.findAllEventosByCadastroFolhaPagamentoQuinzenalId(id);
        return ResponseEntity.ok().body(eventos);
    }

    @GetMapping
    public ResponseEntity<List<CadastroFolhaPagamentoQuinzenal>> findAll() {
        List<CadastroFolhaPagamentoQuinzenal> folhas = cadastroFolhaPagamentoQuinzenalService.findAll();
        return ResponseEntity.ok().body(folhas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cadastroFolhaPagamentoQuinzenalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
