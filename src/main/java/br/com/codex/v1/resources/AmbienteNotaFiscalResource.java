package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.dto.AmbienteNotaFiscalDto;
import br.com.codex.v1.service.AmbienteNotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/api/ambiente_nota_fiscal")
public class AmbienteNotaFiscalResource {

    @Autowired
    private AmbienteNotaFiscalService ambienteNotaFiscalService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<AmbienteNotaFiscalDto> updateAmbiente(@PathVariable Long id, @RequestBody AmbienteNotaFiscalDto dto) {
        AmbienteNotaFiscal ambienteAtualizado = ambienteNotaFiscalService.update(id, dto);
        return ResponseEntity.ok(new AmbienteNotaFiscalDto(ambienteAtualizado));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AmbienteNotaFiscalDto> getAmbienteNotaFiscal(@PathVariable Long id) {
        AmbienteNotaFiscal ambiente = ambienteNotaFiscalService.findById(id);
        return ResponseEntity.ok().body(new AmbienteNotaFiscalDto(ambiente));
    }
}
