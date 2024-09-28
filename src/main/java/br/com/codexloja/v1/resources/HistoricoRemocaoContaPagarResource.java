package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.dto.HistoricoRemocaoContaPagarDto;
import br.com.codexloja.v1.domain.financeiro.HistoricoRemocaoContaPagar;
import br.com.codexloja.v1.service.HistoricoRemocaoContaPagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "v1/api/historico_remove_conta_pagar")
public class HistoricoRemocaoContaPagarResource {

    @Autowired
    private HistoricoRemocaoContaPagarService historicoRemocaoPagarService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO')")
    public ResponseEntity<HistoricoRemocaoContaPagarDto> create(HistoricoRemocaoContaPagarDto historicoRemocaoContaPagarDto) {
        HistoricoRemocaoContaPagar obj = historicoRemocaoPagarService.create(historicoRemocaoContaPagarDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
