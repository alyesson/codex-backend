package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolhaRescisaoRepository extends JpaRepository<FolhaRescisao, Long> {

    Optional<FolhaRescisao> findByNumeroMatricula(String matricula);
}
