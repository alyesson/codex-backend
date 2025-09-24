package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SolicitacaoMaterialRepository extends JpaRepository<SolicitacaoMaterial, Long> {

    @Query("SELECT s FROM SolicitacaoMaterial s WHERE YEAR(s.dataSolicitacao) = :ano AND s.email = :email")
    List<SolicitacaoMaterial> findAllByYearUsuario(@Param("ano") Integer ano, @Param("email") String email);

    @Query("SELECT s FROM SolicitacaoMaterial s WHERE YEAR(s.dataSolicitacao) = :ano")
    List<SolicitacaoMaterial> findAllByYear(@Param("ano") Integer ano);

    @Modifying
    @Transactional
    @Query("UPDATE SolicitacaoMaterial u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao") Integer situacao);

    @Query("SELECT s FROM SolicitacaoMaterial s WHERE s.situacao = :situacao AND YEAR(s.dataSolicitacao) = :anoAtual ORDER BY s.dataSolicitacao DESC")
    List<SolicitacaoMaterial> findAllBySituacao(@Param("situacao") Integer situacao, @Param("anoAtual") int anoAtual);

    @Query("SELECT s FROM SolicitacaoMaterial s WHERE s.dataSolicitacao BETWEEN :dataInicial AND :dataFinal")
    List<SolicitacaoMaterial> findAllSolicitacaoPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}