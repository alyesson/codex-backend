package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoContabilRepository extends JpaRepository<LancamentoContabil, Integer> {

}
