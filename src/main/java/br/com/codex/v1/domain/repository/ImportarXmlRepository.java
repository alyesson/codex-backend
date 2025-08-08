package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.ImportarXml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Query("SELECT i.xml FROM ImportarXml i WHERE i.emissao BETWEEN :dataInicial AND :dataFinal")
    List<String> findAllEntradaNotasPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);

    @Query(value = "SELECT DISTINCT i.cfop FROM importar_xml_itens i " +
            "WHERE EXISTS (SELECT 1 FROM importar_xml n " +
            "WHERE i.numero_nota_fiscal = n.numero " +
            "AND n.emissao BETWEEN :dataInicio AND :dataFim)",
            nativeQuery = true)
    Set<String> findDistinctCfopEntradaByPeriodo(@Param("dataInicio") LocalDate inicio,
                                                 @Param("dataFim") LocalDate fim);
}
