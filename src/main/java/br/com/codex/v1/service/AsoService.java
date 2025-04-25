package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.AsoDto;
import br.com.codex.v1.domain.repository.AsoRepository;
import br.com.codex.v1.domain.rh.Aso;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsoService {

    @Autowired
    private AsoRepository asoRepository;


    public Aso create(AsoDto asoDto) {
        asoDto.setId(null);
        Aso aso = new Aso(asoDto);
        return asoRepository.save(aso);
    }

    public Aso update(Integer id, AsoDto asoDto) {
        asoDto.setId(id);
        Aso obj = findById(id);
        obj = new Aso(asoDto);
        return asoRepository.save(obj);
    }

    public Aso findById(Integer id) {
        Optional<Aso> obj = asoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Aso não encontrado"));
    }

    public void delete(Integer id) {
        asoRepository.deleteById(id);
    }

    public List<Aso> findAll(){
        return asoRepository.findAll();
    }
}
