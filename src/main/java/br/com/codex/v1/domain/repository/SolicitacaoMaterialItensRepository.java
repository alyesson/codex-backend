package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SolicitacaoMaterialItensRepository extends JpaRepository<SolicitacaoMaterialItens, Long> {

    List<SolicitacaoMaterialItens> findBySolicitacaoMaterialId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SolicitacaoMaterialItens u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacaoItens(@Param("id") Long id, @Param("situacao") String situacao);
}
