package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.GerarSpedRequestDto;
import br.com.codex.v1.service.GerarSpedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/sped_efd")
public class SpedEfdResource {

    @Autowired
    private GerarSpedService gerarSpedService;

    @PostMapping("/gerar-efd-icms")
    public ResponseEntity<String> gerarEfdIcms(@RequestBody GerarSpedRequestDto requestDto ) {
        gerarSpedService.gerarBlocos(requestDto);

        String caminhoArquivo = "C:\\SPED\\EFD_ICMS.txt";
        //String caminhoArquivo = "/tmp/sped/EFD_ICMS.txt";
        GerarSpedService.geraBlocos(bloco0, caminhoArquivo);

        return ResponseEntity.ok("SPED gerado com sucesso em: " + caminhoArquivo);
    }
}
