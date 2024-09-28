package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.CotacaoItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotacaoItensCompraRepository extends JpaRepository<CotacaoItensCompra, Integer> {

    List<CotacaoItensCompra> findByCotacaoCompraId(Integer solicitacaoId);
}
