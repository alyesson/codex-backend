package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.VendaItens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaItensRepository extends JpaRepository<VendaItens, Long> {
    List<VendaItens> findByVendaId(Long orcamentoId);
}
