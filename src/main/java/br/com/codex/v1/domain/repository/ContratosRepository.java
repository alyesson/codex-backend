package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.Contratos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratosRepository extends JpaRepository<Contratos, Long> {

    List<Contratos> findAllByOrderByIdDesc();
}
