package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.estoque.SaidaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface SaidaMaterialRepository extends JpaRepository<SaidaMaterial, Integer> {
    @Query(value = "SELECT s FROM SaidaMaterial s WHERE YEAR(s.dataSaida) =:anoAtual")
    List<SaidaMaterial> findAllByYear(@Param("anoAtual") Integer anoAtual);

    @Query("SELECT a FROM SaidaMaterial a WHERE a.dataSaida BETWEEN :dataInicial AND :dataFinal")
    List<SaidaMaterial> findAllSaidaPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);
}
