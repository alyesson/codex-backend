package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface ContaReceberRepository extends JpaRepository<ContaReceber, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ContaReceber c SET c.situacao = :situacao WHERE c.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao") String situacao);

    // Métudo para buscar por ano
    @Query("SELECT c FROM ContaReceber c WHERE YEAR(c.dataVencimento) = :ano")
    List<ContaReceber> findByAno(@Param("ano") int ano);

    // Ou alternativamente, se estiver usando datas específicas:
    @Query("SELECT c FROM ContaReceber c WHERE c.dataVencimento BETWEEN :dataInicio AND :dataFim")
    List<ContaReceber> findByPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
