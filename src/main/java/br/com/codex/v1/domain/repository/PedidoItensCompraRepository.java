package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.PedidoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoItensCompraRepository extends JpaRepository<PedidoItensCompra, Long> {

    List<PedidoItensCompra> findByPedidoCompraId(Long pedidoId);
}
