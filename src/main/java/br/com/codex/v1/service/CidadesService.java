package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Cidades;
import br.com.codex.v1.domain.dto.CidadesDto;
import br.com.codex.v1.domain.repository.CidadesRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadesService {

    @Autowired
    private CidadesRepository cidadesRepository;

    public Cidades create (CidadesDto cidadesDto){
        cidadesDto.setId(null);
        Cidades cidades = new Cidades(cidadesDto);
       return cidadesRepository.save(cidades);
    }

    public Cidades findByCodigo(Integer codigo){
        Optional<Cidades> objCodigo = cidadesRepository.findByCodigoUf(codigo);
        return objCodigo.orElseThrow(() -> new ObjectNotFoundException("Nenhuma cidade encontrada com esse código"));
    }

    public List<Cidades> finAll(){
        return cidadesRepository.findAll();
    }
}
