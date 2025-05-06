package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ConciliacaoContabilDto;
import br.com.codex.v1.service.ConciliacaoContabilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conciliacao_contabil")
public class ConciliacaoContabilController {

    @Autowired
    private ConciliacaoContabilService service;

    @GetMapping
    public List<ConciliacaoContabilDto> listar() {
        return service.listarConciliacoes();
    }
}
