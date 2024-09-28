package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.estoque.HistoricoMovimentacaoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoMovimentacaoMaterialRepository extends JpaRepository<HistoricoMovimentacaoMaterial, Integer> {

    List<HistoricoMovimentacaoMaterial> findAllByOrderByIdDesc();

    @Query("SELECT n FROM HistoricoMovimentacaoMaterial n WHERE YEAR(n.dataEntrada) = :ano AND n.notaFiscal != 0 ORDER BY n.dataEntrada DESC")
    List<HistoricoMovimentacaoMaterial> findAllByNota(@Param("ano") Integer ano);

    @Query("SELECT m FROM HistoricoMovimentacaoMaterial m WHERE YEAR(m.dataEntrada) = :ano and MONTH(m.dataEntrada) = :mes AND m.notaFiscal != 0")
    List<HistoricoMovimentacaoMaterial> findAllByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT MONTH(m.dataEntrada) AS mes, SUM(m.valorNota) AS total FROM HistoricoMovimentacaoMaterial m WHERE YEAR(m.dataEntrada) = :ano AND m.notaFiscal != 0 GROUP BY MONTH(m.dataEntrada)")
    List<Object[]> findAllByYear(@Param("ano") Integer ano);
}
