package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.Afastamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AfastamentoRepository extends JpaRepository<Afastamento, Long> {

    Optional<Afastamento> findByCpf(String cpf);
}
