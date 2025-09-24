package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.SolicitacaoMaterialItensDto;
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

    public SolicitacaoMaterial create(SolicitacaoMaterialDto solicitacaoMaterialDto) {
        LocalDate currentDay = LocalDate.now();
        solicitacaoMaterialDto.setId(null);
        solicitacaoMaterialDto.setDataSolicitacao(currentDay);
        SolicitacaoMaterial solicitacaoMaterial = new SolicitacaoMaterial(solicitacaoMaterialDto);
        solicitacaoMaterial = repository.save(solicitacaoMaterial);

        for(SolicitacaoMaterialItensDto itemDto : solicitacaoMaterialDto.getItens()){
            SolicitacaoMaterialItens item = new SolicitacaoMaterialItens();
            item.setCodigo(itemDto.getCodigo());
            item.setDescricao(itemDto.getDescricao());
            item.setQuantidade(itemDto.getQuantidade());
            item.setUnidadeMedida(itemDto.getUnidadeMedida());
            itensRepository.save(item);
        }
        return solicitacaoMaterial;
    }

    public List<SolicitacaoMaterial> findAllByYear() {
        int currentYear = LocalDate.now().getYear();
        return repository.findAllByYear(currentYear);
    }

    public List<SolicitacaoMaterial> findAllByYearUsuario(String email) {
        int currentYear = LocalDate.now().getYear();
        return repository.findAllByYearUsuario(currentYear, email);
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