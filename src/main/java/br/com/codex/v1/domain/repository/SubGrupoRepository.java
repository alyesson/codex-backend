package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.SubGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubGrupoRepository extends JpaRepository<SubGrupo, Long> {
    Optional<SubGrupo> findByCodigoSubGrupo(String codigoSubGrupo);

    @Query("SELECT g FROM SubGrupo g WHERE g.codigoGrupo = :codigoGrupo")
    List<SubGrupo> findByNomeGrupo(@Param("codigoGrupo") String codigoGrupo);

}
