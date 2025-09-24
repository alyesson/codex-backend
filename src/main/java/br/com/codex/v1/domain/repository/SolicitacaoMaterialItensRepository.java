package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoMaterialItensRepository extends JpaRepository<SolicitacaoMaterialItens, Long> {
    List<SolicitacaoMaterialItens> findBySolicitacaoMaterialId(Long id);
}
