package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByCodigo(String codigo);
}
