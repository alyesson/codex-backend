package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.TiposCategoriasDto;
import br.com.codex.v1.domain.ti.TiposCategorias;
import br.com.codex.v1.service.TiposCategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/tipos_categorias")
public class TiposCategoriasResource {

    @Autowired
    private TiposCategoriasService tiposCategoriasService;

    @GetMapping
    public ResponseEntity<List<TiposCategoriasDto>> findAllTipos(){
        List<TiposCategorias> list = tiposCategoriasService.findAll();
        List<TiposCategoriasDto> listDto = list.stream().map(TiposCategoriasDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
