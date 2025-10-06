package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    @Query("SELECT s FROM Orcamento s WHERE YEAR(s.dataEmissao) = :ano AND s.vendedor = :vendedor")
    List<Orcamento> findAllByYearVendedor(@Param("ano") Integer ano, @Param("vendedor") String vendedor);

    @Query("SELECT s FROM Orcamento s WHERE YEAR(s.dataEmissao) = :ano")
    List<Orcamento> findAllByYear(@Param("ano") Integer ano);

    @Modifying
    @Transactional
    @Query("UPDATE Orcamento u SET u.situacao = :situacao WHERE u.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao")String situacao);

    @Query("SELECT s FROM Orcamento s WHERE s.situacao = 5 AND YEAR(s.dataEmissao) = :anoAtual ORDER BY s.dataEmissao DESC")
    List<Orcamento> findAllBySituacao(@Param("anoAtual") int anoAtual);

    @Query("SELECT s FROM Orcamento s WHERE s.dataEmissao BETWEEN :dataInicial AND :dataFinal")
    List<Orcamento> findAllOrcamentoPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);
}
