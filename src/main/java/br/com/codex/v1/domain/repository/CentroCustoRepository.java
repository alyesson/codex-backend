package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {
    Optional<CentroCusto> findByCodigo(String codigo);
}
