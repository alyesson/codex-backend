package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.ContaReceberDto;
import br.com.codex.v1.domain.financeiro.ContaReceber;
import br.com.codex.v1.domain.repository.ContaReceberRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContasReceberService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    public ContaReceber create(ContaReceberDto contaReceberDto) {
        contaReceberDto.setId(null);
        contaReceberDto.setDataEmissao(java.sql.Date.valueOf(dataAtual));
        ContaReceber contaReceber = new ContaReceber(contaReceberDto);
        return contaReceberRepository.save(contaReceber);
    }

    public ContaReceber update(Long id, ContaReceberDto contaReceberDto) {
        contaReceberDto.setId(id);
        ContaReceber obj = findById(id);
        obj = new ContaReceber(contaReceberDto);
        return contaReceberRepository.save(obj);
    }

    public void updateSituacao(Long id, String situacao) {
        contaReceberRepository.saveSituacao(id, situacao);
    }

    public void delete(Long id) {
        contaReceberRepository.deleteById(id);
    }

    public ContaReceber findById(Long id) {
        Optional<ContaReceber> contaReceberObj = contaReceberRepository.findById(id);
        return contaReceberObj.orElseThrow(() -> new ObjectNotFoundException("Documento não encontrado"));
    }

    public List<ContaReceber> findAll() {
        return contaReceberRepository.findAll();
    }
}
