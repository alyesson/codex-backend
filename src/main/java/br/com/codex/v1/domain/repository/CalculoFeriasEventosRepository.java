package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CalculoFeriasEventos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalculoFeriasEventosRepository extends JpaRepository<CalculoFeriasEventos, Long> {

    void deleteByCalculoFeriasId(Long id);

    List<CalculoFeriasEventos> findAllEventosByCalculoFeriasId(Long eventoId);
}
