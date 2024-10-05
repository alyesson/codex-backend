package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.CriteriosAvaliacao;
import br.com.codex.v1.domain.dto.CriteriosAvaliacaoDto;
import br.com.codex.v1.domain.repository.CriteriosAvaliacaoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriteriosAvaliacaoService {

    @Autowired
    private CriteriosAvaliacaoRepository criteriosAvaliacaoRepository;

    public List<CriteriosAvaliacao> findAll() {
        return criteriosAvaliacaoRepository.findAll();
    }

    public CriteriosAvaliacao findById(Integer id) {
        Optional<CriteriosAvaliacao> objCriterio = criteriosAvaliacaoRepository.findById(id);
        return objCriterio.orElseThrow(() -> new ObjectNotFoundException("Critério de avaliação não encontrado"));
    }

    public CriteriosAvaliacao create(CriteriosAvaliacaoDto criteriosAvaliacaoDto) {

        if(criteriosAvaliacaoDto.getCriterio().isEmpty()){
             throw new DataIntegrityViolationException("O nome do critério não pode ficar em branco");
        }

        if(criteriosAvaliacaoDto.getDescricao().isEmpty()){
            throw new DataIntegrityViolationException("O peso do critério não pode ficar em branco");
        }
            criteriosAvaliacaoDto.setId(null);
            CriteriosAvaliacao objCriterio = new CriteriosAvaliacao(criteriosAvaliacaoDto);
            return criteriosAvaliacaoRepository.save(objCriterio);
    }

    public CriteriosAvaliacao update(Integer id,CriteriosAvaliacaoDto criteriosAvaliacaoDto) {

        if(criteriosAvaliacaoDto.getCriterio().isEmpty()){
            throw new DataIntegrityViolationException("O nome do critério não pode ficar em branco");
        }

        if(criteriosAvaliacaoDto.getDescricao().isEmpty()){
            throw new DataIntegrityViolationException("O peso do critério não pode ficar em branco");
        }

        criteriosAvaliacaoDto.setId(id);
        CriteriosAvaliacao objCriterio = findById(id);
        objCriterio = new CriteriosAvaliacao(criteriosAvaliacaoDto);
        return criteriosAvaliacaoRepository.save(objCriterio);
    }

    public void delete(Integer id) {
        criteriosAvaliacaoRepository.deleteById(id);
    }
}
