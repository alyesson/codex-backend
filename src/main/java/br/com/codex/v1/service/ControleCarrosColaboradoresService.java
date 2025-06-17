package br.com.codex.v1.service;


import br.com.codex.v1.domain.cadastros.ControleCarrosColaboradores;
import br.com.codex.v1.domain.dto.ControleCarrosColaboradoresDto;
import br.com.codex.v1.domain.repository.ControleCarrosColaboradoresRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ControleCarrosColaboradoresService {

    @Autowired
    private ControleCarrosColaboradoresRepository controleCarrosColaboradoresRepository;

    public ControleCarrosColaboradores create(ControleCarrosColaboradoresDto controleCarrosDto) {
        controleCarrosDto.setId(null);
        validaControleCarrosColaboradores(controleCarrosDto);
        ControleCarrosColaboradores centroDeCusto = new ControleCarrosColaboradores(controleCarrosDto);
        return controleCarrosColaboradoresRepository.save(centroDeCusto);
    }
    public ControleCarrosColaboradores update(Long id, ControleCarrosColaboradoresDto controleCarrosDto) {
        controleCarrosDto.setId(id);
        ControleCarrosColaboradores obj = findById(id);
        obj = new ControleCarrosColaboradores(controleCarrosDto);
        return controleCarrosColaboradoresRepository.save(obj);
    }

    public ControleCarrosColaboradores findById(Long id) {
        Optional<ControleCarrosColaboradores> obj = controleCarrosColaboradoresRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Veículo não encontrado"));
    }

    public void delete(Long id) {
        controleCarrosColaboradoresRepository.deleteById(id);
    }

    public List<ControleCarrosColaboradores> findAll(){
        return controleCarrosColaboradoresRepository.findAll();
    }

    private void validaControleCarrosColaboradores(ControleCarrosColaboradoresDto controleCarrosDto){
        Optional<ControleCarrosColaboradores> obj = controleCarrosColaboradoresRepository.findByPlacaVeiculo(controleCarrosDto.getPlacaVeiculo());
        if(obj.isPresent() && obj.get().getPlacaVeiculo().equals(controleCarrosDto.getPlacaVeiculo())){
            throw new DataIntegrityViolationException("Este veículo já foi cadastrado");
        }
    }

    public ControleCarrosColaboradores findByPlacaVeiculo(String placaVeiculo) {
        Optional<ControleCarrosColaboradores> objPlaca = controleCarrosColaboradoresRepository.findByPlacaVeiculo(placaVeiculo);
        return objPlaca.orElseThrow(() -> new ObjectNotFoundException("Veículo não encontrado"));
    }
}
