package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.ImportarXmlItens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportarXmlItensRepository extends JpaRepository<ImportarXmlItens, Long> {

    @Query("SELECT i FROM ImportarXmlItens i WHERE i.numeroNotaFiscal = :numeroNotaFiscal")
    List<ImportarXmlItens> findByNumero(@Param("numeroNotaFiscal") String numeroNotaFiscal);
}
