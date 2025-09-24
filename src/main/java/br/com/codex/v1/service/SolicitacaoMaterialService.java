package br.com.codex.v1.service;

import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import br.com.codex.v1.domain.dto.SolicitacaoMaterialDto;
import br.com.codex.v1.domain.repository.SolicitacaoMaterialItensRepository;
import br.com.codex.v1.domain.repository.SolicitacaoMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitacaoMaterialService {

    @Autowired
    private SolicitacaoMaterialRepository repository;

    @Autowired
    private SolicitacaoMaterialItensRepository itensRepository;

    public SolicitacaoMaterial create(SolicitacaoMaterialDto dto) {
        SolicitacaoMaterial obj = new SolicitacaoMaterial(dto);
        obj = repository.save(obj);
        return obj;
    }

    public List<SolicitacaoMaterial> findAllByYear() {
        int currentYear = LocalDate.now().getYear();
        return repository.findAllByYear(currentYear);
    }

    public List<SolicitacaoMaterial> findAllByYearUsuario(String solicitante) {
        int currentYear = LocalDate.now().getYear();
        return repository.findAllByYearUsuario(currentYear, solicitante);
    }

    public List<SolicitacaoMaterial> findAllBySituacao(Integer situacao) {
        int currentYear = LocalDate.now().getYear();
        return repository.findAllBySituacao(situacao, currentYear);
    }

    public void update(Long id, Integer situacao) {
        repository.saveSituacao(id, situacao);
    }

    public List<SolicitacaoMaterialItens> findAllItensBySolicitacaoId(Long id) {
        return itensRepository.findBySolicitacaoMaterialId(id);
    }

    public SolicitacaoMaterial findById(Long id) {
        Optional<SolicitacaoMaterial> obj = repository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Solicitação de material não encontrada"));
    }

    public List<SolicitacaoMaterial> findAllSolicitacaoPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return repository.findAllSolicitacaoPeriodo(dataInicial, dataFinal);
    }
}