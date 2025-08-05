package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.ImportarXml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ImportarXmlRepository extends JpaRepository<ImportarXml, Long> {

    Optional<ImportarXml> findByChave(String chave);

    boolean existsByChave(String chave);

    @Query("SELECT n FROM ImportarXml n WHERE YEAR(n.dataImportacao) =:anoAtual")
    List<ImportarXml> findAllByYear(@Param("anoAtual") Integer anoAtual);

    @Query("SELECT a FROM ImportarXml a WHERE a.dataImportacao BETWEEN :dataInicial AND :dataFinal")
    List<ImportarXml> findAllEntradaPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

    @Query("SELECT n.id FROM ImportarXml n WHERE n.numero = :numero AND n.razaoSocialEmitente = :razaoSocialEmitente")
    Optional<Long> findIdByNumeroAndRazaoSocialEmitente(@Param("numero") String numero, @Param("razaoSocialEmitente") String razaoSocialEmitente);

    List<ImportarXml> findByEmissaoBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT a FROM ImportarXml a WHERE a.dataImportacao BETWEEN :dataInicial AND :dataFinal")
    List<String> findAllEntradaNotasPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}
