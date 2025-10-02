package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.AlteraSalarioLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AlteraSalarioLoteRepository extends JpaRepository<AlteraSalarioLote, Long> {

    @Query("SELECT a FROM AlteraSalarioLote a WHERE a.dataAlteracao BETWEEN :dataInicial AND :dataFinal")
    List<AlteraSalarioLote> findByDataAlteracao(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);
}
