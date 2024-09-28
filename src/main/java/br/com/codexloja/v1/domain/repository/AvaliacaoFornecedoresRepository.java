package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.AvaliacaoFornecedores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoFornecedoresRepository extends JpaRepository<AvaliacaoFornecedores, Integer> {

    List<AvaliacaoFornecedores> findAll();
}
