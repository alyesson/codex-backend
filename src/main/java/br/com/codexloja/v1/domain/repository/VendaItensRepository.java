package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.vendas.VendaItens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaItensRepository extends JpaRepository<VendaItens, Integer> {
}
