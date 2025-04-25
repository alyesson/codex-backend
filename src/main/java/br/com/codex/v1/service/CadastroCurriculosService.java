package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CadastroCurriculosDto;
import br.com.codex.v1.domain.repository.CadastroCurriculosRepository;
import br.com.codex.v1.domain.rh.CadastroCurriculos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroCurriculosService {

    @Autowired
    private CadastroCurriculosRepository curriculosRepository;


    public CadastroCurriculos create(CadastroCurriculosDto curriculosDto) {
        curriculosDto.setId(null);
        validaCadastroCurriculos(curriculosDto);
        CadastroCurriculos curriculos = new CadastroCurriculos(curriculosDto);
        return curriculosRepository.save(curriculos);
    }

    public CadastroCurriculos update(Integer id, CadastroCurriculosDto curriculosDto) {
        curriculosDto.setId(id);
        CadastroCurriculos obj = findById(id);
        obj = new CadastroCurriculos(curriculosDto);
        return curriculosRepository.save(obj);
    }

    public CadastroCurriculos findById(Integer id) {
        Optional<CadastroCurriculos> obj = curriculosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Curriculo não encontrado"));
    }

    public void delete(Integer id) {
        curriculosRepository.deleteById(id);
    }

    public List<CadastroCurriculos> findAll(){
        return curriculosRepository.findAll();
    }

    private void validaCadastroCurriculos(CadastroCurriculosDto curriculosDto){
        Optional<CadastroCurriculos> obj = curriculosRepository.findByContato(curriculosDto.getContato());
        if(obj.isPresent() && obj.get().getContato().equals(curriculosDto.getContato())){
            throw new DataIntegrityViolationException("O candidato " + curriculosDto.getNome()+ " já foi cadastrado");
        }
    }
}
