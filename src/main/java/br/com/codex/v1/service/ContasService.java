package br.com.codex.v1.service;


import br.com.codex.v1.domain.contabilidade.Contas;
import br.com.codex.v1.domain.dto.ContaSpedDto;
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

    public Contas update(Long id, ContasDto contasDto) {
        contasDto.setId(id);
        Contas obj = findById(id);
        obj = new Contas(contasDto);
        return contasRepository.save(obj);
    }

    public Contas findById(Long id) {
        Optional<Contas> obj = contasRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Conta contábil não encontrada"));
    }

    public void delete(Long id) {
        contasRepository.deleteById(id);
    }

    public List<Contas> findAll(){
        return contasRepository.findAll();
    }

    private void validaContas(ContasDto contasDto){
        //Optional<Contas> objConta = contasRepository.findByConta(contasDto.getConta());
        Optional<Contas> objReduz = contasRepository.findByReduzido(contasDto.getReduzido());

        if(objReduz.isPresent() && objReduz.get().getReduzido().equals(contasDto.getReduzido())){
            throw new DataIntegrityViolationException("Esta conta reduzida já existe");
        }
    }

    public Contas findByNome(String nome) {
        return contasRepository.findByNome(nome)
                .orElseGet(() -> {
                    List<Contas> similares = contasRepository.findByNomeIgnoreCaseContaining(nome);
                    if (!similares.isEmpty()) {
                        return similares.get(0); // retorna o primeiro parecido encontrado
                    }
                    throw new ObjectNotFoundException("Não foi encontrada uma conta com o nome ou semelhante a: " + nome);
                });
    }

    //Busca as informações de uma conta contábil pelo código (ex: "1.1.02.101.2"). Retorna null se não encontrada.
    public Contas buscarContaPorCodigo(String contaContabil) {
        return contasRepository.findByConta(contaContabil);
    }

    //Mét/odo para uso no SPED: busca a conta e retorna um DTO com campos específicos.
    public ContaSpedDto buscarContaParaSped(String contaContabil) {
        Contas conta = contasRepository.findByConta(contaContabil);
        if (conta == null) {
            return null;
        }
        return new ContaSpedDto(
                conta.getConta(),
                conta.getNome(),
                conta.getNatureza(), // Ex: "01" para Ativo
                conta.getTipo(),    // Ex: "S" (Sintética) ou "A" (Analítica)
                conta.getConta().split("\\.").length - 1 // Calcula o nível
        );
    }
}
