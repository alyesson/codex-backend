package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ContaPagar c SET c.situacao = :situacao WHERE c.id = :id")
    void saveSituacao(@Param("id") Long id, @Param("situacao") String situacao);
}
