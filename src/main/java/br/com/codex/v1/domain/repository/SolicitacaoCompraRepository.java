package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface SolicitacaoCompraRepository extends JpaRepository<SolicitacaoCompra, Integer> {

    @Query("SELECT s FROM SolicitacaoCompra s WHERE YEAR(s.dataSolicitacao) = :ano AND s.centroCusto = :centroCustoUsuario AND s.solicitante = :solicitante")
    List<SolicitacaoCompra> findAllByYearUsuario(@Param("ano") Integer ano, @Param("centroCustoUsuario") String centroCustoUsuario, @Param("solicitante") String solicitante);

    @Query("SELECT s FROM SolicitacaoCompra s WHERE YEAR(s.dataSolicitacao) = :ano AND s.centroCusto = :centroCustoUsuario")
    List<SolicitacaoCompra> findAllByYear(@Param("ano") Integer ano, @Param("centroCustoUsuario") String centroCustoUsuario);

    @Modifying
    @Transactional
    @Query("UPDATE SolicitacaoCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Integer id, @Param("situacao")String situacao);

    @Query("SELECT s FROM SolicitacaoCompra s WHERE s.situacao = 'Aprovado' AND YEAR(s.dataSolicitacao) = :anoAtual ORDER BY s.dataSolicitacao DESC")
    List<SolicitacaoCompra> findAllBysituacao(@Param("anoAtual") int anoAtual);

    @Query("SELECT s FROM SolicitacaoCompra s WHERE s.dataSolicitacao BETWEEN :dataInicial AND :dataFinal")
    List<SolicitacaoCompra> findAllSolicitacoesPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);
}
