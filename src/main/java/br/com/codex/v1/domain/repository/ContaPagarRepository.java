package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.ContaPagar;
import br.com.codex.v1.domain.financeiro.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ContaPagar c SET c.situacao = :situacao WHERE c.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao") String situacao);

    // Métudo para buscar por ano
    @Query("SELECT c FROM ContaPagar c WHERE YEAR(c.dataVencimento) = :ano")
    List<ContaPagar> findByAno(@Param("ano") int ano);

    // Ou alternativamente, se estiver usando datas específicas:
    @Query("SELECT c FROM ContaPagar c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim")
    List<ContaPagar> findByPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
