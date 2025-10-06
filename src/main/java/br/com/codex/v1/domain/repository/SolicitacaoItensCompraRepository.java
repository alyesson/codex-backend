package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoItensCompraRepository extends JpaRepository<SolicitacaoItensCompra, Long> {

    List<SolicitacaoItensCompra> findBySolicitacaoCompraId(Long solicitacaoId);
}
