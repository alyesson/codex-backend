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

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/configura_certificado")
public class ConfiguracaoCertificadoResource {

    @Autowired
    private ConfiguracaoCertificadoService configuracaoCertificadoService;

    URI uri = null;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConfiguracaoCertificadoDto> findById(@PathVariable Integer id) {
        ConfiguracaoCertificado obj = configuracaoCertificadoService.findById(id);
        return ResponseEntity.ok().body(new ConfiguracaoCertificadoDto(obj));
    }

    @GetMapping
    public ResponseEntity<List<ConfiguracaoCertificadoDto>> findAll() {
        List<ConfiguracaoCertificado> list = configuracaoCertificadoService.findAll();
        List<ConfiguracaoCertificadoDto> listDto = list.stream().map(ConfiguracaoCertificadoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> create(
            @RequestParam("file") MultipartFile file,
            @RequestParam("senha") String senha) throws Exception {

        // Validação básica
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo não pode estar vazio");
        }
        if (senha == null || senha.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha não pode estar vazia");
        }

        ConfiguracaoCertificado certificado = configuracaoCertificadoService.create(file, senha);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(certificado.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }


    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        configuracaoCertificadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', GERENTE_CONTABILIDADE, CONTABILIDADE)")
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
