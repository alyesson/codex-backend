package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotacaoItensCompraRepository extends JpaRepository<CotacaoItensCompra, Long> {

    List<CotacaoItensCompra> findByCotacaoCompraId(Long solicitacaoId);
}
