package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.NcmDto;
import br.com.codex.v1.service.NcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/ncm")
public class NcmResource {

    @Autowired
    private NcmService ncmService;

    // Para buscar por código (usa consultarNcm)
    @GetMapping("/buscar_por_codigo")
    public ResponseEntity<List<NcmDto>> buscarPorCodigo(@RequestParam String codigo) {
        List<NcmDto> resultados = ncmService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(resultados);
    }
}
