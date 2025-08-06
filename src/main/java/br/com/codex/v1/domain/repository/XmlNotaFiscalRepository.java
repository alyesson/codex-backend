package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface XmlNotaFiscalRepository extends JpaRepository<XmlNotaFiscal, Long> {

    Optional<XmlNotaFiscal> findByChaveAcesso(String chaveAcesso);

    List<XmlNotaFiscal> findByChaveAcessoStartingWith(String s);

    @Query("SELECT x.conteudoXml FROM XmlNotaFiscal x WHERE x.dataCriacao BETWEEN :inicioDoDiaInicial AND :fimDoDiaFinal")
    List<String> findAllNotasSaidasPeriodo(@Param("inicioDoDiaInicial") LocalDateTime inicioDoDiaInicial, @Param("fimDoDiaFinal") LocalDateTime fimDoDiaFinal);
}
