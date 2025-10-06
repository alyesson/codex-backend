package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.EspelhoPontoDto;
import br.com.codex.v1.domain.dto.ProcessamentoFolhaDto;
import br.com.codex.v1.domain.repository.EspelhoPontoRepository;
import br.com.codex.v1.domain.rh.EspelhoPonto;
import br.com.codex.v1.domain.rh.ImportacaoPontoResultado;
import br.com.codex.v1.service.EspelhoPontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/espelho_ponto")
@Validated
public class EspelhoPontoResource {

    @Autowired
    private EspelhoPontoService espelhoPontoService;

    @Autowired
    private EspelhoPontoRepository espelhoPontoRepository;

    @PostMapping("/importar")
        public ResponseEntity<ImportacaoPontoResultado> importarArquivoPonto(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        try {
            ImportacaoPontoResultado resultado = espelhoPontoService.importarArquivoPonto(arquivo, dataInicio, dataFim);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/processar-folha")
    public ResponseEntity<ProcessamentoFolhaDto> processarFolhaPonto(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        ProcessamentoFolhaDto resultado = espelhoPontoService.processarFolhaPonto(dataInicio, dataFim);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EspelhoPontoDto>> buscarEspelhoPonto(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "colaboradorId", required = false) Long colaboradorId) {

        List<EspelhoPontoDto> espelhos = espelhoPontoService.buscarEspelhoPonto(dataInicio, dataFim, colaboradorId);
        return ResponseEntity.ok(espelhos);
    }

    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<EspelhoPontoDto>> buscarPorDepartamento(
            @PathVariable String departamento,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<EspelhoPonto> espelhos = espelhoPontoRepository.findByDepartamentoAndPeriodo(departamento, dataInicio, dataFim);
        List<EspelhoPontoDto> dtos = espelhos.stream().map(EspelhoPontoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/colaborador/{pis}")
    public ResponseEntity<List<EspelhoPontoDto>> buscarPorPis(
            @PathVariable String pis,
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<EspelhoPonto> espelhos = espelhoPontoRepository.findByPisAndPeriodo(pis, dataInicio, dataFim);
        List<EspelhoPontoDto> dtos = espelhos.stream().map(EspelhoPontoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
