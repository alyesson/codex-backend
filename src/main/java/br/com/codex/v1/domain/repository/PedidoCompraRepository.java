package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    //List<PedidoCompra> findAll();

    @Modifying
    @Transactional
    @Query("UPDATE PedidoCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao") String situacao);
}
