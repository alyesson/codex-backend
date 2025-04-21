package br.com.codex.v1.service;


import br.com.codex.v1.domain.contabilidade.Contas;
import br.com.codex.v1.domain.dto.ContasDto;
import br.com.codex.v1.domain.repository.ContasRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContasService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    
    @Autowired
    private ContasRepository contasRepository;

    public Contas create(ContasDto contasDto) {
        contasDto.setId(null);
        contasDto.setInclusao(java.sql.Date.valueOf(dataAtual));
        validaContas(contasDto);
        Contas contas = new Contas(contasDto);
        return contasRepository.save(contas);
    }

    public Contas update(Integer id, ContasDto contasDto) {
        contasDto.setId(id);
        Contas obj = findById(id);
        obj = new Contas(contasDto);
        return contasRepository.save(obj);
    }

    public Contas findById(Integer id) {
        Optional<Contas> obj = contasRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Conta contábil não encontrada"));
    }

    public void delete(Integer id) {
        contasRepository.deleteById(id);
    }

    public List<Contas> findAll(){
        return contasRepository.findAll();
    }

    private void validaContas(ContasDto contasDto){
        Optional<Contas> objConta = contasRepository.findByConta(contasDto.getConta());
        Optional<Contas> objReduz = contasRepository.findByReduzido(contasDto.getReduzido());

        if(objConta.isPresent() && objConta.get().getConta().equals(contasDto.getConta())){
            throw new DataIntegrityViolationException("Esta conta contábil já existe");
        }

        if(objReduz.isPresent() && objReduz.get().getReduzido().equals(contasDto.getReduzido())){
            throw new DataIntegrityViolationException("Esta conta reduzida já existe");
        }
    }

    public Contas findByNome(String clientes) {
        Optional<Contas> objNome = contasRepository.findByNomeIgnoreCaseContaining(clientes);
        return objNome.orElseGet(() -> objNome.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrada uma conta débito ou crédito com o nome " + objNome)));
    }
}
