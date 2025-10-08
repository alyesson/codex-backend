package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroFolhaPagamentoQuinzenalRepository extends JpaRepository<CadastroFolhaPagamentoQuinzenal, Long> {
}
