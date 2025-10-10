package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaQuinzenaEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolhaQuinzenalEventosRepository extends JpaRepository<FolhaQuinzenaEventos, Long> {

    List<FolhaQuinzenaEventos> findAllEventosByCadastroFolhaPagamentoQuinzenalId(Long eventoId);

    void deleteByCadastroFolhaPagamentoQuinzenalId(Long id);
}
