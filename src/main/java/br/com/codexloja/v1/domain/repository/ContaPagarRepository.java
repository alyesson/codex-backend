package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.financeiro.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ContaPagarRepository extends JpaRepository<ContaPagar, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE ContaPagar c SET c.situacao = :situacao WHERE c.id = :id")
    void saveSituacao(@Param("id") Integer id, @Param("situacao") String situacao);
}
