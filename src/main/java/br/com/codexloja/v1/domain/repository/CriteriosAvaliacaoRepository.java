package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.CriteriosAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriteriosAvaliacaoRepository extends JpaRepository<CriteriosAvaliacao, Integer> {

    List<CriteriosAvaliacao> findAll();
}
