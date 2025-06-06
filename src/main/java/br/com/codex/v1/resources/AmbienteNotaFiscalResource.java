package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.dto.AmbienteNotaFiscalDto;
import br.com.codex.v1.service.AmbienteNotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/api/ambiente_nota_fiscal")
public class AmbienteNotaFiscalResource {

    @Autowired
    private AmbienteNotaFiscalService ambienteNotaFiscalService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<AmbienteNotaFiscalDto> getAmbienteNotaFiscal(@Valid @RequestBody Integer id, AmbienteNotaFiscalDto ambienteNotaFiscalDto) {
        AmbienteNotaFiscal ambienteNotaFiscal = ambienteNotaFiscalService.update(id, ambienteNotaFiscalDto);
        AmbienteNotaFiscalDto dto = new AmbienteNotaFiscalDto(ambienteNotaFiscal);
        return ResponseEntity.ok().body(dto);
    }
}
