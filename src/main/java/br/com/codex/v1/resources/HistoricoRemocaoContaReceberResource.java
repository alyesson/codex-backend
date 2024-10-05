package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.HistoricoRemocaoContaReceberDto;
import br.com.codex.v1.domain.financeiro.HistoricoRemocaoContaReceber;
import br.com.codex.v1.service.HistoricoRemocaoContaReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "v1/api/historico_remove_conta_receber")
public class HistoricoRemocaoContaReceberResource {

    @Autowired
    private HistoricoRemocaoContaReceberService historicoRemocaoReceberService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO')")
    public ResponseEntity<HistoricoRemocaoContaReceberDto> create(HistoricoRemocaoContaReceberDto historicoRemocaoContaReceberDto) {
        HistoricoRemocaoContaReceber obj = historicoRemocaoReceberService.create(historicoRemocaoContaReceberDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
