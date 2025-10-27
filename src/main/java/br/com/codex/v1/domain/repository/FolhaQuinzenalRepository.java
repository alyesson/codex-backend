package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolhaQuinzenalRepository extends JpaRepository<FolhaQuinzenal, Long> {

    Optional<FolhaQuinzenal> findByMatriculaColaborador(String numeroMatricula);
}
