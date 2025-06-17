package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedores;
import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;
import br.com.codex.v1.domain.dto.AvaliacaoFornecedoresDto;
import br.com.codex.v1.domain.dto.AvaliacaoFornecedoresDetalhesDto;
import br.com.codex.v1.domain.repository.AvaliacaoFornecedoresItensRepository;
import br.com.codex.v1.domain.repository.AvaliacaoFornecedoresRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoFornecedoresService {
    
    @Autowired
    private AvaliacaoFornecedoresRepository avaliacaoFornecedoresRepository;
    
    @Autowired
    private AvaliacaoFornecedoresItensRepository avaliacaoFornecedoresItensRepository;

    public AvaliacaoFornecedores create(AvaliacaoFornecedoresDto avaliacaoFornecedoresdto) {
        avaliacaoFornecedoresdto.setId(null);
        AvaliacaoFornecedores objAvaliacaoFornecedores = new AvaliacaoFornecedores(avaliacaoFornecedoresdto);
        objAvaliacaoFornecedores = avaliacaoFornecedoresRepository.save(objAvaliacaoFornecedores);

        // Salvando os critérios da avaliação dos fornecedores
        for (AvaliacaoFornecedoresDetalhesDto itemDto : avaliacaoFornecedoresdto.getItens()) {
            AvaliacaoFornecedoresDetalhes item = new AvaliacaoFornecedoresDetalhes();
            item.setCriterio(itemDto.getCriterio());
            item.setPeso(itemDto.getPeso());
            item.setNota(itemDto.getNota());
            item.setAvaliacaoFornecedores(objAvaliacaoFornecedores);
            avaliacaoFornecedoresItensRepository.save(item);
        }
        return objAvaliacaoFornecedores;
    }

    public List<AvaliacaoFornecedoresDetalhes> findAllItensByAvaliacaoId(Long avaliacaoId) {
        return avaliacaoFornecedoresItensRepository.findByAvaliacaoFornecedoresId(avaliacaoId);
    }

    public List<AvaliacaoFornecedores> findAll() {
        return avaliacaoFornecedoresRepository.findAll();
    }

    public AvaliacaoFornecedores findById(Long id) {
        Optional<AvaliacaoFornecedores> objAvaliacaoFornecedores = avaliacaoFornecedoresRepository.findById(id);
        return objAvaliacaoFornecedores.orElseThrow(() -> new ObjectNotFoundException("Avaliação não encontrada"));
    }

    public void deleteById(Long id) {
        avaliacaoFornecedoresRepository.deleteById(id);
    }
}
