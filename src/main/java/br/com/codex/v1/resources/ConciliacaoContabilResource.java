package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ConciliacaoContabilDto;
import br.com.codex.v1.service.ConciliacaoContabilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/api/conciliacao_contabil")
public class ConciliacaoContabilResource {

    @Autowired
    private ConciliacaoContabilService conciliacaoContabilService;

    @GetMapping
    public List<ConciliacaoContabilDto> listarConciliacoesMesCorrente() {
        return conciliacaoContabilService.listarConciliacoesMesCorrente();
    }

    @GetMapping("/por_periodo")
    public List<ConciliacaoContabilDto> listarPorPeriodo(@RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        return conciliacaoContabilService.listarConciliacoesPorPeriodo(inicio, fim);
    }
}
