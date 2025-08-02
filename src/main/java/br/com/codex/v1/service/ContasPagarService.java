package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.ContaPagarDto;
import br.com.codex.v1.domain.dto.GrupoDto;
import br.com.codex.v1.domain.estoque.Grupo;
import br.com.codex.v1.domain.financeiro.ContaPagar;
import br.com.codex.v1.domain.repository.ContaPagarRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Year;
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

    public ContaPagar update(Long id, ContaPagarDto contapagarDto) {
        contapagarDto.setId(id);
        ContaPagar obj = findById(id);
        obj = new ContaPagar(contapagarDto);
        return contaPagarRepository.save(obj);
    }

    public void updateSituacao(Long id, String situacao) {
        contaPagarRepository.saveSituacao(id, situacao);
    }

    public void delete(Long id) {
        contaPagarRepository.deleteById(id);
    }

    public ContaPagar findById(Long id) {
        Optional<ContaPagar> contaPagarObj = contaPagarRepository.findById(id);
        return contaPagarObj.orElseThrow(() -> new ObjectNotFoundException("Documento não encontrado"));
    }

    public List<ContaPagar> findAll() {
        // Obtém o ano atual
        int anoAtual = Year.now().getValue();

        // Retorna apenas os registros do ano atual
        return contaPagarRepository.findByAno(anoAtual);
    }
}
