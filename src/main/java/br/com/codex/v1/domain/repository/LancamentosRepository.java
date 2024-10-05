package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentosRepository extends JpaRepository<Lancamentos, Integer> {
}
