package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {

    @Query("SELECT s FROM PedidoCompra s WHERE YEAR(s.dataPedido) = :ano")
    List<PedidoCompra> findAllByYear(@Param("ano") Integer ano);

    @Modifying
    @Transactional
    @Query("UPDATE PedidoCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao")String situacao);

    @Query("SELECT c FROM PedidoCompra c WHERE c.dataPedido BETWEEN :dataInicial AND :dataFinal")
    List<PedidoCompra> findAllPedidosPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}
