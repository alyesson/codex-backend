package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.ImportarXmlItens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImportarXmlItensRepository extends JpaRepository<ImportarXmlItens, Integer> {

    @Query("SELECT i FROM ImportarXmlItens i WHERE i.numeroNotaFiscal = :numeroNotaFiscal")
    List<ImportarXmlItens> findByNumero(@Param("numeroNotaFiscal") String numeroNotaFiscal);
}
