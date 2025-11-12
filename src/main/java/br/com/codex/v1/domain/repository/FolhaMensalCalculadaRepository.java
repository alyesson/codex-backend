package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolhaMensalCalculadaRepository extends JpaRepository<FolhaMensalCalculada, Long> {

    Optional<FolhaMensalCalculada>findByMatriculaColaborador(String numeroMatricula);

    @Query("SELECT f FROM FolhaMensal f WHERE f.matriculaColaborador = :matricula AND MONTH(f.dataProcessamento) = :mes AND YEAR(f.dataProcessamento) = :ano")
    Optional<FolhaMensal> findByMatriculaColaboradorAndMesAndAno(@Param("matricula") String matriculaColaborador, @Param("mes") Integer mes, @Param("ano") Integer ano);
}
