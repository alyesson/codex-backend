package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
    void deleteByNomeReserva(String nomeReserva);

    @Transactional
    void deleteByNomeReservaAndId(String nomeReserva, Integer id);
}
