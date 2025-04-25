package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.ControlePortaria;
import br.com.codex.v1.domain.dto.ControlePortariaDto;
import br.com.codex.v1.service.ControlePortariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/controle_portaria")
public class ControlePortariaResource {

    @Autowired
    private ControlePortariaService controlePortariaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO', 'PORTARIA')")
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestParam("nome") String nome,
                                         @RequestParam("cpf") String cpf,
                                         @RequestParam("empresa") String empresa,
                                         @RequestParam("visitado") String visitado,
                                         @RequestParam("horaEntrada") String horaEntrada,
                                         @RequestParam("veiculo") String veiculo,
                                         @RequestParam("marca") String marca,
                                         @RequestParam("cor") String cor,
                                         @RequestParam("placa") String placa,
                                         @RequestParam("observacao") String observacao,
                                         @RequestParam("autorEntrada") String autorEntrada,
                                         @RequestParam(value = "imagem", required = false) MultipartFile imagem){

        try{

            ControlePortariaDto controlePortariaDto = new ControlePortariaDto();
            controlePortariaDto.setNome(nome);
            controlePortariaDto.setCpf(cpf);
            controlePortariaDto.setEmpresa(empresa);
            controlePortariaDto.setVisitado(visitado);
            controlePortariaDto.setHoraEntrada(horaEntrada);
            controlePortariaDto.setVeiculo(veiculo);
            controlePortariaDto.setMarca(marca);
            controlePortariaDto.setCor(cor);
            controlePortariaDto.setPlaca(placa);
            controlePortariaDto.setObservacao(observacao);
            controlePortariaDto.setAutorEntrada(autorEntrada);

            if (imagem != null) {
                controlePortariaDto.setImagem(imagem.getBytes());
            }else{
                controlePortariaDto.setImagem(null);
            }

            ControlePortaria obj = controlePortariaService.create(controlePortariaDto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
        return ResponseEntity.badRequest().body("Erro ao salvar cadastro de visitante: "+e);
    }

    }
    /*public ResponseEntity<ControlePortariaDto> create(@Valid @RequestBody ControlePortariaDto controlePortariaDto){
        ControlePortaria obj = controlePortariaService.create(controlePortariaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }*/

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO', 'PORTARIA')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ControlePortariaDto> update(@PathVariable Integer id, @Valid @RequestBody ControlePortariaDto controlePortariaDto){
        ControlePortaria obj = controlePortariaService.update(id, controlePortariaDto);
        return ResponseEntity.ok().body(new ControlePortariaDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ADMINISTRATIVO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ControlePortariaDto> delete(@PathVariable Integer id){
        controlePortariaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ControlePortariaDto> findById(@PathVariable Integer id){
        ControlePortaria objGrupo = controlePortariaService.findById(id);
        return ResponseEntity.ok().body(new ControlePortariaDto(objGrupo));
    }

    @GetMapping
    public ResponseEntity<List<ControlePortariaDto>> findAll(){
        List<ControlePortaria> list = controlePortariaService.findAll();
        List<ControlePortariaDto> listDto = list.stream().map(ControlePortariaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI', 'GERENTE_ADMINISTRATIVO', 'PORTARIA')")
    @GetMapping(value = "/controle_periodo")
    public ResponseEntity<List<ControlePortariaDto>> findAllControlePeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<ControlePortaria> list = controlePortariaService.findAllControlePeriodo(dataInicial, dataFinal);
        List<ControlePortariaDto> listDto = list.stream().map(ControlePortariaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
