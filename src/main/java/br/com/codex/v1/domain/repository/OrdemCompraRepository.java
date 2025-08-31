package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.OrdemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface OrdemCompraRepository extends JpaRepository<OrdemCompra, Long> {

    @Query("SELECT s FROM OrdemCompra s WHERE YEAR(s.dataSolicitacao) = :ano AND s.centroCusto = :centroCustoUsuario AND s.solicitante = :solicitante")
    List<OrdemCompra> findAllByYearUsuario(@Param("ano") Integer ano, @Param("centroCustoUsuario") String centroCustoUsuario, @Param("solicitante") String solicitante);

    @Query("SELECT s FROM OrdemCompra s WHERE YEAR(s.dataSolicitacao) = :ano")
    List<OrdemCompra> findAllByYear(@Param("ano") Integer ano);

    @Modifying
    @Transactional
    @Query("UPDATE OrdemCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao")String situacao);

    @Query("SELECT s FROM OrdemCompra s WHERE s.situacao = 'Aprovado' AND YEAR(s.dataSolicitacao) = :anoAtual ORDER BY s.dataSolicitacao DESC")
    List<OrdemCompra> findAllBysituacao(@Param("anoAtual") int anoAtual);

    @Query("SELECT s FROM OrdemCompra s WHERE s.dataSolicitacao BETWEEN :dataInicial AND :dataFinal")
    List<OrdemCompra> findAllOrdemPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);
}
