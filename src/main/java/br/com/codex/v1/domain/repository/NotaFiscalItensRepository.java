package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.NotaFiscalItens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaFiscalItensRepository extends JpaRepository<NotaFiscalItens, Integer> {

    @Query("SELECT i FROM NotaFiscalItens i WHERE i.numeroNotaFiscal = :numeroNotaFiscal")
    List<NotaFiscalItens> findByNumero(@Param("numeroNotaFiscal") String numeroNotaFiscal);
}
