package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.Servicos;
import br.com.codex.v1.domain.dto.ServicosDto;
import br.com.codex.v1.domain.repository.ServicosRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicosService {

    @Autowired
    private ServicosRepository servicosRepository;


    public Servicos create(ServicosDto servicosDto) {
        servicosDto.setId(null);
        validaServicos(servicosDto);
        Servicos servicos = new Servicos(servicosDto);
        return servicosRepository.save(servicos);
    }

    public Servicos update(Long id, ServicosDto servicosDto) {
        servicosDto.setId(id);
        Servicos obj = findById(id);
        obj = new Servicos(servicosDto);
        return servicosRepository.save(obj);
    }

    public Servicos findById(Long id) {
        Optional<Servicos> obj = servicosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Serviço não encontrado"));
    }

    public Servicos findByCodigo(String codigo){
        Optional<Servicos> objServico = servicosRepository.findByCodigo(codigo);
        return objServico.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrado nenhum serviço de nome " +codigo));
    }

    public void delete(Long id) {
        servicosRepository.deleteById(id);
    }

    public List<Servicos> findAll(){
        return servicosRepository.findAll();
    }

    private void validaServicos(ServicosDto servicosDto){
        Optional<Servicos> obj = servicosRepository.findByCodigo(servicosDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(servicosDto.getCodigo())){
            throw new DataIntegrityViolationException("Já existe um serviço cadastrado com o código "+servicosDto.getCodigo());
        }
    }
}
