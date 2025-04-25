package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.SalaReuniao;
import br.com.codex.v1.domain.dto.SalaReuniaoDto;
import br.com.codex.v1.domain.repository.SalaReuniaoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaReuniaoService {

    @Autowired
    private SalaReuniaoRepository salaReuniaoRepository;


    public SalaReuniao create(SalaReuniaoDto salaReuniaoDto) {
        salaReuniaoDto.setId(null);
        SalaReuniao salaReuniao = new SalaReuniao(salaReuniaoDto);
        return salaReuniaoRepository.save(salaReuniao);
    }

    public SalaReuniao update(Integer id, SalaReuniaoDto salaReuniaoDto) {
        salaReuniaoDto.setId(id);
        SalaReuniao obj = findById(id);
        obj = new SalaReuniao(salaReuniaoDto);
        return salaReuniaoRepository.save(obj);
    }

    public SalaReuniao findById(Integer id) {
        Optional<SalaReuniao> obj = salaReuniaoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Sala de Reuniao não encontrada"));
    }

    public void delete(Integer id) {
        salaReuniaoRepository.deleteById(id);
    }

    public List<SalaReuniao> findAll(){
        return salaReuniaoRepository.findAll();
    }
}
