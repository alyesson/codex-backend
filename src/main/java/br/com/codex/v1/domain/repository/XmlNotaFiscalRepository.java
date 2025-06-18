package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XmlNotaFiscalRepository extends JpaRepository<XmlNotaFiscal, Long> {

    Optional<XmlNotaFiscal> findByChaveAcesso(String chaveAcesso);
}
