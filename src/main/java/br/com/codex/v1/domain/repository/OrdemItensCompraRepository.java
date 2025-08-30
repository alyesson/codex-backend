package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.OrdemItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemItensCompraRepository extends JpaRepository<OrdemItensCompra, Long> {

    List<OrdemItensCompra> findByOrdemCompraId(Long solicitacaoId);
}
