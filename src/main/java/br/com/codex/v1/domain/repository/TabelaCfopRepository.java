package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TabelaCfopRepository extends JpaRepository<TabelaCfop, Long> {

    @Query("SELECT c FROM TabelaCfop c WHERE c.fluxo = :fluxo")
    List<TabelaCfop> findByFluxo(@Param("fluxo") String fluxo);

    @Query("SELECT t FROM TabelaCfop t WHERE t.codigo IN :codigos")
    List<TabelaCfop> findByCodigos(@Param("codigos") Set<Integer> codigos);
}
