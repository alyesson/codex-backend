package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.Contratos;
import br.com.codex.v1.domain.dto.AtendimentosDto;
import br.com.codex.v1.domain.dto.ContratosDto;
import br.com.codex.v1.domain.ti.Atendimentos;
import br.com.codex.v1.service.AtendimentosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/atendimentos_ti")
public class AtendimentosResource {

    @Autowired
    private AtendimentosService atendimentosService;

   /* @PostMapping
    public ResponseEntity<AtendimentosDto> create(@Valid @RequestBody AtendimentosDto atendimentosDto){
        Atendimentos obj = atendimentosService.create(atendimentosDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }*/

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestParam("solicitante") String solicitante,
                                         @RequestParam("email") String email,
                                         @RequestParam("telefone") String telefone,
                                         @RequestParam("departamento") String departamento,
                                         @RequestParam("titulo") String titulo,
                                         @RequestParam("problema") String problema,
                                         @RequestParam("categoria") String categoria,
                                         @RequestParam("tipo") String tipo,
                                         @RequestParam("prioridade") Integer prioridade,
                                         @RequestParam("dataAbertura") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAbertura,
                                         @RequestParam(value = "dataFechamento", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFechamento,
                                         @RequestParam("diasAtuacao") String diasAtuacao,
                                         @RequestParam("horaInicio") String horaInicio,
                                         @RequestParam("horaFim") String horaFim,
                                         @RequestParam("resolucao") String resolucao,
                                         @RequestParam("nomeTecnico") String nomeTecnico,
                                         @RequestParam("situacao") Integer situacao,
                                         @RequestParam(value = "imagem", required = false) MultipartFile imagem){
        try {
            // Cria um objeto Documento com os dados do formulário
            AtendimentosDto atendimentos = new AtendimentosDto();
            atendimentos.setSolicitante(solicitante);
            atendimentos.setEmail(email);
            atendimentos.setTelefone(telefone);
            atendimentos.setDepartamento(departamento);
            atendimentos.setTitulo(titulo);
            atendimentos.setProblema(problema);
            atendimentos.setCategoria(categoria);
            atendimentos.setTipo(tipo);
            atendimentos.setPrioridade(prioridade);
            atendimentos.setDataAbertura(dataAbertura);
            atendimentos.setDataFechamento(dataFechamento);
            atendimentos.setDiasAtuacao(diasAtuacao);
            atendimentos.setHoraInicio(horaInicio);
            atendimentos.setHoraFim(horaFim);
            atendimentos.setResolucao(resolucao);
            atendimentos.setNomeTecnico(nomeTecnico);
            atendimentos.setSituacao(situacao);

            if (imagem != null) {
                atendimentos.setImagem(imagem.getBytes());
            }else{
                atendimentos.setImagem(null);
            }

            Atendimentos objDoc = atendimentosService.create(atendimentos);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDoc.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar atendimento: "+e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_TI', 'TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<AtendimentosDto> update(@PathVariable Long id, @Valid @RequestBody AtendimentosDto atendimentosDto){
        Atendimentos obj = atendimentosService.update(id, atendimentosDto);
        return ResponseEntity.ok().body(new AtendimentosDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AtendimentosDto> delete(@PathVariable Long id){
        atendimentosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AtendimentosDto> findById(@PathVariable Long id){
        Atendimentos objAtendimento = atendimentosService.findById(id);
        return ResponseEntity.ok().body(new AtendimentosDto(objAtendimento));
    }

    @GetMapping(value = "/chamados_mes_ano")
    public ResponseEntity<List<AtendimentosDto>> findAllYearAndMonth(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "mes") Integer mes, @RequestParam(value = "solicitante") String solicitante){
        List<Atendimentos> list = atendimentosService.findAllYearAndMonth(ano, mes, solicitante);
        List<AtendimentosDto> listDto = list.stream().map(AtendimentosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/chamados_ano")
    public ResponseEntity<List<AtendimentosDto>> findAllYear(@RequestParam(value = "ano") Integer ano){
        List<Atendimentos> list = atendimentosService.findAllYear(ano);
        List<AtendimentosDto> listDto = list.stream().map(AtendimentosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Atendimento TI Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI')")
    @GetMapping(value = "/atendimentos_periodo")
    public ResponseEntity<List<AtendimentosDto>> findAllAtendimentosPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Atendimentos> list = atendimentosService.findAllAtendimentosPeriodo(dataInicial, dataFinal);
        List<AtendimentosDto> listDto = list.stream().map(AtendimentosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Soma a quantidade de atendimentos abertos do dia
    @GetMapping(value = "/atendimentos_abertos")
    public ResponseEntity<Integer> somaAtendimentosAbertos(){
        int totalAtendimento = atendimentosService.somaAtendimentosAbertos();
        return ResponseEntity.ok(totalAtendimento);
    }

    //Soma a quantidade de atendimentos em andamento do dia
    @GetMapping(value = "/atendimentos_em_andamento")
    public ResponseEntity<Integer> somaAtendimentosEmAndamento(){
        int totalAtendimento = atendimentosService.somaAtendimentosEmAndamento();
        return ResponseEntity.ok(totalAtendimento);
    }
}
