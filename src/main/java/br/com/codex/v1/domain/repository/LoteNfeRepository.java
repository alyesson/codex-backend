package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.LoteNfe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoteNfeRepository extends JpaRepository<LoteNfe, Long> {

    @Query("SELECT l FROM LoteNfe l WHERE l.idLote = :serie AND l.cnpjEmitente = :cnpj AND l.ambiente = :ambiente")
    Optional<LoteNfe> findByIdLoteAndCnpjAndAmbiente(@Param("serie") String serie, @Param("cnpj") String cnpjEmitente, @Param("ambiente") Integer ambiente);
}
