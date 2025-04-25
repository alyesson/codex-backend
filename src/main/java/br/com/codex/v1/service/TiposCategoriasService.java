package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.TiposCategoriasRepository;
import br.com.codex.v1.domain.ti.TiposCategorias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiposCategoriasService {

    @Autowired
    private TiposCategoriasRepository tiposCategoriasRepository;

    public List<TiposCategorias> findAll(){
        return tiposCategoriasRepository.findAll();
    }
}
