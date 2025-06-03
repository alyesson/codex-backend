package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.dto.ConfiguracaoCertificadoDto;
import br.com.codex.v1.service.ConfiguracaoCertificadoService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/configura_certificado")
public class ConfiguracaoCertificadoResource {

    @Autowired
    private ConfiguracaoCertificadoService configuracaoCertificadoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ConfiguracaoCertificadoDto> findById(@PathVariable Integer id) {
        ConfiguracaoCertificado obj = configuracaoCertificadoService.findById(id);
        return ResponseEntity.ok().body(new ConfiguracaoCertificadoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping
    public ResponseEntity<List<ConfiguracaoCertificadoDto>> findAll() {
        List<ConfiguracaoCertificado> list = configuracaoCertificadoService.findAll();
        List<ConfiguracaoCertificadoDto> listDto = list.stream().map(ConfiguracaoCertificadoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestParam("nome") String nomeContrato,
                                         @RequestParam("tipo") String tipo,
                                         @RequestParam("dataValidade") Date dataValidade,
                                         @RequestParam("razaoSocial") String razaoSocial,
                                         @RequestParam("cnpj") String cnpj,
                                         @RequestParam("dataCadastro") LocalDateTime dataCadastro,
                                         @RequestParam("ativo") boolean ativo,
                                         @RequestParam(value = "arquivo", required = false) MultipartFile arquivo) {

        try {
            ConfiguracaoCertificadoDto documento = new ConfiguracaoCertificadoDto();
            documento.setNome(nomeContrato);
            documento.setTipo(tipo);
            documento.setDataValidade(dataValidade);
            documento.setRazaoSocial(razaoSocial);
            documento.setCnpj(cnpj);
            documento.setDataCadastro(dataCadastro);
            documento.setRazaoSocial(razaoSocial);
            documento.setAtivo(ativo);

            if (arquivo != null) {
                documento.setArquivo(arquivo.getBytes());
            }else{
                documento.setArquivo(null);
            }

            ConfiguracaoCertificado objDoc = configuracaoCertificadoService.create(documento);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objDoc.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar certificado: "+e);
        }
    }


    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        configuracaoCertificadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/downloads/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Integer id) {
        ConfiguracaoCertificado objDoc = configuracaoCertificadoService.findById(id);
        byte[] fileBytes = objDoc.getArquivo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "certificado_"+objDoc.getNome());
        headers.setContentLength(fileBytes.length);

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}
