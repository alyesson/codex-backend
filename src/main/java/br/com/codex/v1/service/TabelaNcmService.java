package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.TabelaNcm;
import br.com.codex.v1.domain.dto.TabelaNcmDto;
import br.com.codex.v1.domain.repository.TabelaNcmRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TabelaNcmService {

    @Autowired
    private TabelaNcmRepository tabelaNcmRepository;

    public TabelaNcm create (TabelaNcmDto tabelaNcmDto){
        tabelaNcmDto.setId(null);
        TabelaNcm tabelaNcm = new TabelaNcm(tabelaNcmDto);
       return tabelaNcmRepository.save(tabelaNcm);
    }

    public TabelaNcm findByCodigo(String codigo){
        Optional<TabelaNcm> objCodigo = tabelaNcmRepository.findByCodigo(codigo);
        return objCodigo.orElseThrow(() -> new ObjectNotFoundException("Nenhuma Ncm encontrada com esse código"));
    }

    public List<TabelaNcm> finAll(){
        return tabelaNcmRepository.findAll();
    }

    public TabelaNcm findById(Integer id) {
        Optional<TabelaNcm> objCodigo = tabelaNcmRepository.findById(id);
        return objCodigo.orElseThrow(() -> new ObjectNotFoundException("Nenhuma Ncm encontrada com esse id"));
    }
}
