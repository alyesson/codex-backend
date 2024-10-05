package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.LancamentosContas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface LancamentosContasRepository extends JpaRepository<LancamentosContas, Integer> {

    List<LancamentosContas> findByLancamentosId(Integer lancamentoId);

    @Query("SELECT l FROM LancamentosContas l WHERE l.dataLancamento BETWEEN :dataInicio AND :dataFim")
    List<LancamentosContas> findAllByYearAndMonth(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

}
