package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Visitantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitantesRepository extends JpaRepository<Visitantes, Long> {
    Optional<Visitantes> findByCpf(String cpf);

    @Query("SELECT v FROM Visitantes v WHERE v.dataCadastro BETWEEN :dataInicial AND :dataFinal")
    List<Visitantes> findAllVisitantesPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

}
