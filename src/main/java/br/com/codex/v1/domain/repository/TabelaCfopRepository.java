package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TabelaCfopRepository extends JpaRepository<TabelaCfop, Integer> {

    @Query("SELECT c FROM TabelaCfop c WHERE c.tipoCfop = :tipoCfop")
    List<TabelaCfop> findByTipoCfop(@Param("tipoCfop") String tipoCfop);

}
