package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.PedidoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItensCompraRepository extends JpaRepository<PedidoItensCompra, Long> {

    List<PedidoItensCompra> findByPedidoCompraId(Long pedidoId);
}
