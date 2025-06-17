package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Departamento;
import br.com.codex.v1.domain.dto.DepartamentoDto;
import br.com.codex.v1.domain.repository.DepartamentoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Departamento create(DepartamentoDto subgrupoDto) {
        subgrupoDto.setId(null);
        validaDepartamento(subgrupoDto);
        Departamento grupo = new Departamento(subgrupoDto);
        return departamentoRepository.save(grupo);
    }
    public Departamento update(Long id, DepartamentoDto subgrupoDto) {
        subgrupoDto.setId(id);
        Departamento obj = findById(id);
        obj = new Departamento(subgrupoDto);
        return departamentoRepository.save(obj);
    }

    public Departamento findById(Long id) {
        Optional<Departamento> obj = departamentoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Departamento não encontrado"));
    }

    public void delete(Long id) {
        departamentoRepository.deleteById(id);
    }

    public List<Departamento> findAll(){
        return departamentoRepository.findAll();
    }

    private void validaDepartamento(DepartamentoDto subgrupoDto){
        Optional<Departamento> obj = departamentoRepository.findByCodigo(subgrupoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(subgrupoDto.getCodigo())){
            throw new DataIntegrityViolationException("Este departamento de custo já existe");
        }
    }
}
