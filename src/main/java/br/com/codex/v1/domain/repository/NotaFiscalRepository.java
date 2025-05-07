package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.NotasFiscais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotaFiscalRepository extends JpaRepository<NotasFiscais, Integer> {

    Optional<NotasFiscais> findByChave(String chave);

    boolean existsByChave(String chave);

    @Query("SELECT n FROM NotasFiscais n WHERE YEAR(n.dataImportacao) =:anoAtual")
    List<NotasFiscais> findAllByYear(@Param("anoAtual") Integer anoAtual);

    @Query("SELECT a FROM NotasFiscais a WHERE a.dataImportacao BETWEEN :dataInicial AND :dataFinal")
    List<NotasFiscais> findAllEntradaPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

    @Query("SELECT n.id FROM NotasFiscais n WHERE n.numero = :numero AND n.razaoSocialEmitente = :razaoSocialEmitente")
    Optional<Integer> findIdByNumeroAndRazaoSocialEmitente(@Param("numero") String numero, @Param("razaoSocialEmitente") String razaoSocialEmitente);

    List<NotasFiscais> findByEmissaoBetween(LocalDate inicio, LocalDate fim);
}
