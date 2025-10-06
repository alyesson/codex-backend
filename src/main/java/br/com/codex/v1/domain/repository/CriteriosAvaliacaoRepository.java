package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.CriteriosAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriosAvaliacaoRepository extends JpaRepository<CriteriosAvaliacao, Long> {

    List<CriteriosAvaliacao> findAll();
}
