package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface CotacaoCompraRepository extends JpaRepository<CotacaoCompra, Long> {

    @Query("SELECT s FROM CotacaoCompra s WHERE YEAR(s.dataAbertura) = :ano")
    List<CotacaoCompra> findAllByYear(@Param("ano") Integer ano);

    @Modifying
    @Transactional
    @Query("UPDATE CotacaoCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao")String situacao);

    @Query("SELECT c FROM CotacaoCompra c WHERE c.dataAbertura BETWEEN :dataInicial AND :dataFinal")
    List<CotacaoCompra> findAllCotacoesPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query("SELECT c FROM CotacaoCompra c WHERE YEAR(c.dataAbertura) = :ano AND c.situacao = :situacao")
    List<CotacaoCompra> findAllBySituacaoAndYear(@Param("ano") Integer ano, @Param("situacao") String situacao);
}
