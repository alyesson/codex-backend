package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface CotacaoCompraRepository extends JpaRepository<CotacaoCompra, Integer> {

    List<CotacaoCompra> findAll();

    @Modifying
    @Transactional
    @Query("UPDATE CotacaoCompra u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Integer id, @Param("situacao")String situacao);

    @Query("SELECT c FROM CotacaoCompra  c WHERE c.dataSolicitacao BETWEEN :dataInicial AND :dataFinal")
    List<CotacaoCompra> findAllCotacoesPeriodo(@Param("dataInicial") java.util.Date dataInicial, @Param("dataFinal") Date dataFinal);
}
