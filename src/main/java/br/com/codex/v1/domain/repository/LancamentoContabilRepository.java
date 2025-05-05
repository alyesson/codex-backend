package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.estoque.NotasFiscais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface LancamentoContabilRepository extends JpaRepository<LancamentoContabil, Integer> {

    @Query("SELECT l FROM LancamentoContabil l WHERE YEAR(l.dataLancamento) = :ano AND MONTH(l.dataLancamento) = :mes")
    List<LancamentoContabil> findAllByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT l FROM LancamentoContabil l WHERE l.dataLancamento BETWEEN :dataInicio AND :dataFim")
    List<LancamentoContabil> findAllByYearRange(@Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim);

    Optional<NotasFiscais> findByNumeroAndRazaoSocialEmitente(String numero, String razaoSocialEmitente);
}
