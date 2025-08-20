package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    @Query("SELECT v FROM Venda v WHERE YEAR(v.dataEmissao) = :ano")
    List<Venda> findAllByYear(@Param("ano") Integer ano);

    @Query("SELECT v FROM Venda v WHERE YEAR(v.dataEmissao) = :ano AND MONTH(v.dataEmissao) = :mes")
    List<Venda> findAllByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COUNT(v) FROM Venda v WHERE YEAR(v.dataEmissao) = :ano AND MONTH(v.dataEmissao) = :mes")
    int countByDataVenda(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT v FROM Venda v WHERE v.dataEmissao BETWEEN :dataInicial AND :dataFinal")
    List<Venda> findAllVendaPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query("SELECT v.vendedor, COUNT(v), SUM(v.valorFinal) AS totalVendas FROM Venda v " +
            "WHERE v.dataEmissao BETWEEN :dataInicial AND :dataFinal GROUP BY v.vendedor ORDER BY totalVendas DESC")
    List<Object[]> findVendedoresByNumeroVendas(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query("SELECT v.consumidor, SUM(v.valorFinal) AS totalVendas FROM Venda v " +
            "WHERE v.dataEmissao BETWEEN :dataInicial AND :dataFinal GROUP BY v.consumidor ORDER BY totalVendas DESC")
    List<Object[]> findByVendasClientes(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query("SELECT s FROM Venda s WHERE s.situacao = 4 AND YEAR(s.dataEmissao) = :anoAtual ORDER BY s.id DESC")
    List<Venda> findAllBySituacao(@Param("anoAtual") int anoAtual);

    @Query("SELECT v FROM Venda v WHERE YEAR(v.dataEmissao) = :ano AND v.vendedor = :vendedor ORDER BY v.id DESC")
    List<Venda> findByYearAndVendedor(@Param("ano") Integer ano, @Param("vendedor") String vendedor);

    @Query("SELECT v FROM Venda v WHERE YEAR(v.dataEmissao) = :ano AND MONTH(v.dataEmissao) = :mes AND v.vendedor = :vendedor ORDER BY v.vendedor")
    List<Venda> findAllByMonthAndVendedor(@Param("ano") Integer ano, @Param("mes") Integer mes, @Param("vendedor") String vendedor);

}