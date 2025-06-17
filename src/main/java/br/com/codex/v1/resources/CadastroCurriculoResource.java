package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.Contratos;
import br.com.codex.v1.domain.dto.CadastroCurriculosDto;
import br.com.codex.v1.domain.dto.ContratosDto;
import br.com.codex.v1.domain.rh.CadastroCurriculos;
import br.com.codex.v1.service.CadastroCurriculosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/cadastro_curriculos")
public class CadastroCurriculoResource {

    @Autowired
    private CadastroCurriculosService cadastroCurriculosService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestParam("nome") String nome,
                                         @RequestParam("sexo") String sexo,
                                         @RequestParam("contato") String contato,
                                         @RequestParam("escolaridade") String escolaridade,
                                         @RequestParam("areaFormacao") String areaFormacao,
                                         @RequestParam("cidade") String cidade,
                                         @RequestParam("situacao") String situacao,
                                         @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {

        try {
            // Cria um objeto Documento com os dados do formulário
            CadastroCurriculosDto documento = new CadastroCurriculosDto();
            documento.setNome(nome);
            documento.setSexo(sexo);
            documento.setContato(contato);
            documento.setEscolaridade(escolaridade);
            documento.setAreaFormacao(areaFormacao);
            documento.setCidade(cidade);
            documento.setSituacao(situacao);

            if (arquivo != null) {
                documento.setArquivo(arquivo.getBytes());
            }else{
                documento.setArquivo(null);
            }

            CadastroCurriculos objDoc = cadastroCurriculosService.create(documento);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDoc.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar currículo: "+e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CadastroCurriculosDto> update(@PathVariable Long id, @Valid @RequestBody CadastroCurriculosDto cadastroCurriculosDto){
        CadastroCurriculos objCadastroCurriculos = cadastroCurriculosService.update(id, cadastroCurriculosDto);
        return ResponseEntity.ok().body(new CadastroCurriculosDto((objCadastroCurriculos)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CadastroCurriculosDto> delete(@PathVariable Long id){
        cadastroCurriculosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CadastroCurriculosDto> findById(@PathVariable Long id){
        CadastroCurriculos objCadastroCurriculos = cadastroCurriculosService.findById(id);
        return ResponseEntity.ok().body(new CadastroCurriculosDto(objCadastroCurriculos));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping
    public ResponseEntity<List<CadastroCurriculosDto>> findAll(){
        List<CadastroCurriculos> list = cadastroCurriculosService.findAll();
        List<CadastroCurriculosDto> listDto = list.stream().map(CadastroCurriculosDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/downloads/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long id) {
        CadastroCurriculos objDoc = cadastroCurriculosService.findById(id);
        byte[] fileBytes = objDoc.getArquivo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", objDoc.getNome());
        headers.setContentLength(fileBytes.length);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
