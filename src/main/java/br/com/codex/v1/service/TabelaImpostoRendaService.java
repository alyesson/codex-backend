package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.TabelaImpostoRendaDto;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TabelaImpostoRendaService {

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    public TabelaImpostoRenda create(TabelaImpostoRendaDto tabelaImpostoRendaDto) {
        tabelaImpostoRendaDto.setId(null);
        TabelaImpostoRenda centroDeCusto = new TabelaImpostoRenda(tabelaImpostoRendaDto);
        return tabelaImpostoRendaRepository.save(centroDeCusto);
    }

    public TabelaImpostoRenda update(Long id, TabelaImpostoRendaDto tabelaImpostoRendaDto) {
        tabelaImpostoRendaDto.setId(id);
        TabelaImpostoRenda obj = findById(id);
        obj = new TabelaImpostoRenda(tabelaImpostoRendaDto);
        return tabelaImpostoRendaRepository.save(obj);
    }

    public TabelaImpostoRenda findById(Long id) {
        Optional<TabelaImpostoRenda> obj = tabelaImpostoRendaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Incidência de imposto de renda não encontrado"));
    }

    public void delete(Long id) {
        tabelaImpostoRendaRepository.deleteById(id);
    }

    public List<TabelaImpostoRenda> findAll(){
        return tabelaImpostoRendaRepository.findAll();
    }

    public BigDecimal getSalarioMinimo() {
        return tabelaImpostoRendaRepository.findTopByOrderById()
                .orElseThrow(() -> new RuntimeException("Tabela de IRRF não encontrada"))
                .getSalarioMinimo();
    }
}
