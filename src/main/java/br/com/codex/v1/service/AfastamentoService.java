package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.AfastamentoDto;
import br.com.codex.v1.domain.repository.AfastamentoRepository;
import br.com.codex.v1.domain.rh.Afastamento;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AfastamentoService {

    @Autowired
    private AfastamentoRepository afastamentoRepository;

    public Afastamento create(AfastamentoDto afastamentoDto) {
        afastamentoDto.setId(null);
        Afastamento centroDeCusto = new Afastamento(afastamentoDto);
        return afastamentoRepository.save(centroDeCusto);
    }

    public Afastamento update(Long id, AfastamentoDto afastamentoDto) {
        afastamentoDto.setId(id);
        Afastamento obj = findById(id);
        obj = new Afastamento(afastamentoDto);
        return afastamentoRepository.save(obj);
    }

    public Afastamento findById(Long id) {
        Optional<Afastamento> obj = afastamentoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Ativo imobilizado não encontrado"));
    }

    public void delete(Long id) {
        afastamentoRepository.deleteById(id);
    }

    public List<Afastamento> findAll(){
        return afastamentoRepository.findAll();
    }

    public Afastamento findByCpf(String cpf){
        Optional<Afastamento> objCpf = afastamentoRepository.findByCpf(cpf);
        return objCpf.orElseThrow(() -> new ObjectNotFoundException("Nenhum funcionário como o cpf "+  cpf +" foi encontrado"));
    }
}
