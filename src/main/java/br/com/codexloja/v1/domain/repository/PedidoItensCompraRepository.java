package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.PedidoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItensCompraRepository extends JpaRepository<PedidoItensCompra, Integer> {

    List<PedidoItensCompra> findByPedidoCompraId(Integer pedidoId);
}
