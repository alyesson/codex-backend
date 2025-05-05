package br.com.codex.v1.domain.repository;

import br.com.codex.v1.utilitario.RecoverUserPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecoverUserPasswordRepository extends JpaRepository<RecoverUserPassword, UUID> {
}
