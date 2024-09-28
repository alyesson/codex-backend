package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.cadastros.CentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CentroCustoRepository extends JpaRepository<CentroCusto, Integer> {
    Optional<CentroCusto> findByCodigo(String codigo);
}
