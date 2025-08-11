package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.Servicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicosRepository extends JpaRepository<Servicos, Long> {

    Optional<Servicos> findByCodigo(String codigo);
}
