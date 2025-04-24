package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    Optional<Pessoa> findByCpf(String cpf);

    Optional<Pessoa> findByEmail(String email);
}
