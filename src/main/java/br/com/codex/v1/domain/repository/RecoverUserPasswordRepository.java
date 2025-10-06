package br.com.codex.v1.domain.repository;

import br.com.codex.v1.utilitario.RecoverUserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecoverUserPasswordRepository extends JpaRepository<RecoverUserPassword, UUID> {
}
