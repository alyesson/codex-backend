package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.ti.Atendimentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface AtendimentosRepository extends JpaRepository<Atendimentos, Integer> {

    @Query("SELECT c FROM Atendimentos c WHERE YEAR(c.dataAbertura) = :ano and MONTH(c.dataAbertura) = :mes AND c.solicitante=:solicitante")
    List<Atendimentos> findAllYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes, @Param("solicitante") String solicitante);

    @Query("SELECT c FROM Atendimentos c WHERE YEAR(c.dataAbertura) = :ano")
    List<Atendimentos> findAllYear(@Param("ano") Integer ano);

    @Query("SELECT a FROM Atendimentos a WHERE a.dataAbertura BETWEEN :dataInicial AND :dataFinal")
    List<Atendimentos> findAllAtendimentosPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    int countBySituacao(Situacao anEnum);
}
