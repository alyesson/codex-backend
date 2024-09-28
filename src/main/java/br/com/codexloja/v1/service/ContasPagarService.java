package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.dto.ContaPagarDto;
import br.com.codexloja.v1.domain.financeiro.ContaPagar;
import br.com.codexloja.v1.domain.repository.ContaPagarRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContasPagarService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    @Autowired
    private ContaPagarRepository contaPagarRepository;

    public ContaPagar create(ContaPagarDto contaPagarDto) {
        contaPagarDto.setId(null);
        contaPagarDto.setDataEmissao(java.sql.Date.valueOf(dataAtual));
        ContaPagar contaPagar = new ContaPagar(contaPagarDto);
        return contaPagarRepository.save(contaPagar);
    }

    public void update(Integer id, String situacao) {
        contaPagarRepository.saveSituacao(id, situacao);
    }

    public void delete(Integer id) {
        contaPagarRepository.deleteById(id);
    }

    public ContaPagar findById(Integer id) {
        Optional<ContaPagar> contaPagarObj = contaPagarRepository.findById(id);
        return contaPagarObj.orElseThrow(() -> new ObjectNotFoundException("Documento não encontrado"));
    }

    public List<ContaPagar> findAll() {
        return contaPagarRepository.findAll();
    }
}
