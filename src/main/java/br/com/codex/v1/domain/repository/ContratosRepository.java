package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.Contratos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratosRepository extends JpaRepository<Contratos, Integer> {

    List<Contratos> findAllByOrderByIdDesc();
}
