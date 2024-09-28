package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.dto.ContaReceberDto;
import br.com.codexloja.v1.domain.financeiro.ContaReceber;
import br.com.codexloja.v1.domain.repository.ContaReceberRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
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

    public void update(Integer id, String situacao) {
        contaReceberRepository.saveSituacao(id, situacao);
    }

    public void delete(Integer id) {
        contaReceberRepository.deleteById(id);
    }

    public ContaReceber findById(Integer id) {
        Optional<ContaReceber> contaReceberObj = contaReceberRepository.findById(id);
        return contaReceberObj.orElseThrow(() -> new ObjectNotFoundException("Documento não encontrado"));
    }

    public List<ContaReceber> findAll() {
        return contaReceberRepository.findAll();
    }
}
