package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CadastroColaboradoresDto;
import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CadastroColaboradoresService {
    
    @Autowired
    CadastroColaboradoresRepository cadastroColaboradoresRepository;

    public CadastroColaboradores create(CadastroColaboradoresDto cadastroColaboradoresDto) {
        cadastroColaboradoresDto.setId(null);
        CadastroColaboradores centroDeCusto = new CadastroColaboradores(cadastroColaboradoresDto);
        return cadastroColaboradoresRepository.save(centroDeCusto);
    }

    public CadastroColaboradores update(Long id, CadastroColaboradoresDto cadastroColaboradoresDto) {
        cadastroColaboradoresDto.setId(id);
        CadastroColaboradores obj = findById(id);
        obj = new CadastroColaboradores(cadastroColaboradoresDto);
        return cadastroColaboradoresRepository.save(obj);
    }

    public CadastroColaboradores findById(Long id) {
        Optional<CadastroColaboradores> obj = cadastroColaboradoresRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Funcionário não encontrado"));
    }

    public CadastroColaboradores findByCpf(String cpf) {
        Optional<CadastroColaboradores> obj = cadastroColaboradoresRepository.findByCpf(cpf);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Funcionário não encontrado"));
    }

    public void delete(Long id) {
        cadastroColaboradoresRepository.deleteById(id);
    }

    public List<CadastroColaboradores> findAll(){
        return cadastroColaboradoresRepository.findAll();
    }

    public Map<String, Long> countBySituacaoAtual() {
        List<Object[]> resultados = cadastroColaboradoresRepository.countBySituacaoAtualAgrupado();
        Map<String, Long> contagem = new HashMap<>();

        for (Object[] resultado : resultados) {
            String situacao = (String) resultado[0];
            Long quantidade = (Long) resultado[1];
            contagem.put(situacao, quantidade);
        }
        return contagem;
    }

    public List<CadastroColaboradores> findAllColaboradoresAtivos(String situacaoAtual) {
        return cadastroColaboradoresRepository.findAllColaboradoresComSituacaoContratado();
    }

    public List<CadastroColaboradores> findAllColaboradoresComSituacaoDesligado(String situacaoAtual) {
        return cadastroColaboradoresRepository.findAllColaboradoresComSituacaoDesligado();
    }

    public CadastroColaboradores findByNomeColaborador(String nomeColaborador) {
        Optional<CadastroColaboradores> objColaboradores = cadastroColaboradoresRepository.findByNomeColaborador(nomeColaborador);

        return objColaboradores.orElseThrow(() ->
                new ObjectNotFoundException("O funcionário " + nomeColaborador + " não foi encontrado na base de dados")
        );
    }

    @Transactional
    public CadastroColaboradores alteraSalario(String cpf, BigDecimal novoSalario, BigDecimal novoSalarioHora) {
        CadastroColaboradores colaborador = cadastroColaboradoresRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado com CPF: " + cpf));

        colaborador.setSalarioColaborador(novoSalario);
        colaborador.setSalarioHora(novoSalarioHora);

        return cadastroColaboradoresRepository.save(colaborador);
    }
}
