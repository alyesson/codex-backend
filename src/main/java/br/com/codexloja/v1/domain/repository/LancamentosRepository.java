package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.contabilidade.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentosRepository extends JpaRepository<Lancamentos, Integer> {
}
