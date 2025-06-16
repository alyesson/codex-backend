package br.com.codex.v1.resources;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.dto.TabelaCfopDto;
import br.com.codex.v1.domain.financeiro.CentroCusto;
import br.com.codex.v1.service.TabelaCfopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/lista_cfop")
public class TabelaCfopResource {

    @Autowired
    private TabelaCfopService tabelaCfopService;

    public ResponseEntity<List<TabelaCfopDto>> findByTipoCfop(String tipoCfop) {
        List<TabelaCfop> list = tabelaCfopService.findByTipoCfop(tipoCfop);
        List<TabelaCfopDto> listDto = list.stream().map(TabelaCfopDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
