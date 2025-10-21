package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.CalculoFeriasRepository;
import br.com.codex.v1.domain.rh.CalculoFerias;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalculoFeriasService {

    @Autowired
    private CalculoFeriasRepository calculoFeriasRepository;

    public CalculoFerias findByNumeroMatricula(String matricula){
        Optional<CalculoFerias> obj = calculoFeriasRepository.findByNumeroMatricula(matricula);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação não encontrada"));
    }
}
