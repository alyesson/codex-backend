package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.SolicitacaoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoItensCompraRepository extends JpaRepository<SolicitacaoItensCompra, Integer> {

    List<SolicitacaoItensCompra> findBySolicitacaoCompraId(Integer solicitacaoId);
}
