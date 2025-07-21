package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.Fornecedores;
import br.com.codex.v1.domain.dto.FornecedoresDto;
import br.com.codex.v1.domain.repository.FornecedoresRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedoresService {

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    public Fornecedores create(FornecedoresDto fornecedoresDto) {
        fornecedoresDto.setId(null);
        validaCnpj(fornecedoresDto);
        Fornecedores objFornec = new Fornecedores(fornecedoresDto);
        return fornecedoresRepository.save(objFornec);
    }

    public Fornecedores update(Long id, FornecedoresDto fornecedoresDto) {
        fornecedoresDto.setId(id);
        Fornecedores objFornec = findById(id);
        objFornec = new Fornecedores(fornecedoresDto);
        return fornecedoresRepository.save(objFornec);
    }

    public void delete( Long id){
        fornecedoresRepository.deleteById(id);
    }

    public Fornecedores findById(Long id){
        Optional<Fornecedores> objFornec = fornecedoresRepository.findById(id);
        return objFornec.orElseThrow(() -> new ObjectNotFoundException("Fornecedor não encontrado"));
    }

    public List<Fornecedores> findAll(){
        return fornecedoresRepository.findAll();
    }

    private void validaCnpj(FornecedoresDto fornecedoresDto){
        Optional<Fornecedores> fornecedores = fornecedoresRepository.findByCnpj(fornecedoresDto.getCnpj());
        if(fornecedores.isPresent() && fornecedores.get().getCnpj().equals(fornecedoresDto.getCnpj())){
            throw new DataIntegrityViolationException("Já existe um fornecedor cadastrado com este CNPJ");
        }
    }

    public Fornecedores findByCnpj(String cnpj) {
        Optional<Fornecedores> objFornec = fornecedoresRepository.findByCnpj(cnpj);
        return objFornec.orElseThrow(() -> new ObjectNotFoundException("Fornecedor não encontrado"));
    }
}
