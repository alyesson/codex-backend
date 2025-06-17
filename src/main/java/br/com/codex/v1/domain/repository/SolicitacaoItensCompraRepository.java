package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoItensCompraRepository extends JpaRepository<SolicitacaoItensCompra, Long> {

    List<SolicitacaoItensCompra> findBySolicitacaoCompraId(Long solicitacaoId);
}
