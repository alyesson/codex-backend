package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.EspelhoPontoDto;
import br.com.codex.v1.domain.dto.ProcessamentoFolhaDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.CadastroJornadaRepository;
import br.com.codex.v1.domain.repository.EspelhoPontoRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.CadastroJornada;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/espelho_ponto")
@Validated
public class EspelhoPontoResource {

    @Autowired
    private EspelhoPontoService espelhoPontoService;

    @Autowired
    private EspelhoPontoRepository espelhoPontoRepository;

    @Autowired
    private CadastroColaboradoresRepository colaboradoresRepository;

    @Autowired
    private CadastroJornadaRepository jornadaRepository;

    @PostMapping("/salvar")
    public ResponseEntity<List<EspelhoPontoDto>> salvarEspelhos(@RequestBody List<EspelhoPontoDto> espelhosDto) {
        try {
            if (espelhosDto == null || espelhosDto.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<EspelhoPonto> entidades = espelhosDto.stream()
                    .map(dto -> {
                        if (dto.getColaborador() == null || dto.getColaborador().getNumeroPis() == null) {
                            return null; // pula se não tiver PIS
                        }

                        CadastroColaboradores colab = colaboradoresRepository.findByNumeroPis(dto.getColaborador().getNumeroPis());
                        if (colab == null) {
                            System.out.println("Colaborador com PIS " + dto.getColaborador().getNumeroPis() + " não encontrado, pulando.");
                            return null;
                        }

                        EspelhoPonto e = new EspelhoPonto();
                        e.setColaborador(colab);
                        e.setData(dto.getData());
                        e.setEntrada(dto.getEntrada());
                        e.setSaidaAlmoco(dto.getSaidaAlmoco());
                        e.setRetornoAlmoco(dto.getRetornoAlmoco());
                        e.setSaida(dto.getSaida());
                        e.setHorasTrabalhadasMinutos(dto.getHorasTrabalhadasMinutos());
                        e.setHorasExtrasMinutos(dto.getHorasExtrasMinutos());
                        e.setHorasFaltantesMinutos(dto.getHorasFaltantesMinutos());
                        e.setCustoHorasExtras(dto.getCustoHorasExtras());
                        e.setHorasDeveriaTrabalharMinutos(dto.getHorasDeveriaTrabalharMinutos());
                        e.setSituacao(dto.getSituacao() != null ? dto.getSituacao() : Situacao.ATIVO);
                        e.setObservacoes(dto.getObservacoes());

                        return e;
                    })
                    .filter(Objects::nonNull) // remove os que retornaram null
                    .collect(Collectors.toList());

            // Salva no banco
            List<EspelhoPonto> salvos = espelhoPontoRepository.saveAll(entidades);

            // Converte para DTOs
            List<EspelhoPontoDto> retorno = salvos.stream()
                    .map(EspelhoPontoDto::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(retorno);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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
