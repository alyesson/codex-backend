package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.ControlePortaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ControlePortariaRepository extends JpaRepository<ControlePortaria, Integer> {

    @Query("SELECT c FROM ControlePortaria c WHERE c.dataEntrada BETWEEN :dataInicial AND :dataFinal")
    List<ControlePortaria> findAllControlePeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}
