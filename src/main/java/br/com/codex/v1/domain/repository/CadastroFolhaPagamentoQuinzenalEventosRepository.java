package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenalEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CadastroFolhaPagamentoQuinzenalEventosRepository extends JpaRepository<CadastroFolhaPagamentoQuinzenalEventos, Long> {

    List<CadastroFolhaPagamentoQuinzenalEventos> findAllEventosByCadastroFolhaPagamentoQuinzenalId(Long eventoId);

    void deleteByCadastroFolhaPagamentoQuinzenalId(Long id);
}
