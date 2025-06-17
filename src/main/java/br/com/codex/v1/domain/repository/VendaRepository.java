package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda,Long> {
    @Query("SELECT v FROM Venda v WHERE YEAR(v.dataVenda) = :ano")
    List<Venda> findAllByYear(@Param("ano") Integer ano);

    @Query("SELECT m FROM Venda m WHERE YEAR(m.dataVenda) = :ano and MONTH(m.dataVenda) = :mes")
    List<Venda> findAllByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COUNT(m.dataVenda) FROM Venda m WHERE YEAR(m.dataVenda) = :ano and MONTH(m.dataVenda) = :mes")
    int countByDataVenda(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT v FROM Venda v WHERE v.dataVenda BETWEEN :dataInicial AND :dataFinal")
    List<Venda> findAllVendaPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

    @Query("SELECT v.vendedor, COUNT(v), SUM(v.totalVenda) AS totalVendas FROM Venda v " +
            "WHERE v.dataVenda BETWEEN :dataInicial AND :dataFinal GROUP BY v.vendedor ORDER BY totalVendas DESC")
    List<Object[]> findVendedoresByNumeroVendas(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

    @Query("SELECT v.cliente, SUM(v.totalVenda) AS totalVendas FROM Venda v " +
            "WHERE v.dataVenda BETWEEN :dataInicial AND :dataFinal GROUP BY v.cliente ORDER BY totalVendas DESC")
    List<Object[]> findByVendasClientes(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

}
