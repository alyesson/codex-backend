package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.dto.ContaBancariaDto;
import br.com.codexloja.v1.domain.financeiro.ContaBancaria;
import br.com.codexloja.v1.domain.repository.ContaBancariaRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaBancariaService {

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    public ContaBancaria create(ContaBancariaDto contaBancariaDto) {
        contaBancariaDto.setId(null);
        validaContas(contaBancariaDto);
        ContaBancaria grupo = new ContaBancaria(contaBancariaDto);
        return contaBancariaRepository.save(grupo);
    }

    public ContaBancaria findById(Integer id) {
        Optional<ContaBancaria> obj = contaBancariaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Conta contábil não encontrada"));
    }

    public void delete(Integer id) {
        contaBancariaRepository.deleteById(id);
    }

    public List<ContaBancaria> findAll(){
        return contaBancariaRepository.findAll();
    }

    private void validaContas(ContaBancariaDto contasDto){
        Optional<ContaBancaria> objConta = contaBancariaRepository.findByNome(contasDto.getNome());

        if(objConta.isPresent() && objConta.get().getNome().equals(contasDto.getNome())){
            throw new DataIntegrityViolationException("Esta conta já existe");
        }
    }
}
