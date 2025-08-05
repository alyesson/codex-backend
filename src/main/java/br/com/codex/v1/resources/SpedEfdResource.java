package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.domain.dto.GerarSpedRequestDto;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.service.GerarSpedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/api/sped_efd")
public class SpedEfdResource {

    @PostMapping("/gerar-efd-icms")
    public ResponseEntity<String> gerarEfdIcms(@RequestBody GerarSpedRequestDto requestDto ) {
        // 1. Extrai os dados do request
        EmpresaDto empresa = requestDto.getEmpresa();
        List<NotaFiscalDto> notasFiscais = requestDto.getNotasFiscais();

        // 2. Gera o Bloco 0 (Dados da empresa e participantes)
        //Bloco0 bloco0 = Bloco0Service.getBloco(notasFiscais, empresa);

        // 3. Chama o gerador do SPED (EFD-ICMS)
        String caminhoArquivo = "C:\\SPED\\EFD_ICMS.txt";
        //String caminhoArquivo = "/tmp/sped/EFD_ICMS.txt";
        GerarSpedService.geraBlocos(bloco0, caminhoArquivo);

        return ResponseEntity.ok("SPED gerado com sucesso em: " + caminhoArquivo);
    }
}
