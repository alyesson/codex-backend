package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CadastroCargosDto;
import br.com.codex.v1.domain.repository.CadastroCargosRepository;
import br.com.codex.v1.domain.rh.CadastroCargos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroCargosService {

    @Autowired
    private CadastroCargosRepository cadastroCargosRepository;

    public CadastroCargos create (CadastroCargosDto cadastroCargosDto){
        cadastroCargosDto.setId(null);
        validaCadastroCargos(cadastroCargosDto);
        CadastroCargos objCargos = new CadastroCargos(cadastroCargosDto);
        return cadastroCargosRepository.save(objCargos);
    }

    public CadastroCargos update(Long id, CadastroCargosDto cadastroCargosDto) {
        cadastroCargosDto.setId(id);
        CadastroCargos obj = findById(id);
        obj = new CadastroCargos(cadastroCargosDto);
        return cadastroCargosRepository.save(obj);
    }

    public CadastroCargos findById(Long id) {
        Optional<CadastroCargos> obj = cadastroCargosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Cargo não encontrado"));
    }

    public void delete(Long id) {
        cadastroCargosRepository.deleteById(id);
    }

    public List<CadastroCargos> findAll(){
        return cadastroCargosRepository.findAll();
    }

    private void validaCadastroCargos(CadastroCargosDto cadastroCargosDto) {
        Optional<CadastroCargos> obj = cadastroCargosRepository.findByCodigoCargo(cadastroCargosDto.getCodigoCargo());
        if (obj.isPresent() && obj.get().getCodigoCargo().equals(cadastroCargosDto.getCodigoCargo())) {
            throw new DataIntegrityViolationException("Este cargo já foi cadastrado");
        }
    }
}
