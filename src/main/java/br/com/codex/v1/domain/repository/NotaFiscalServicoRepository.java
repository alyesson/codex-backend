package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotaFiscalServicoRepository extends JpaRepository<NotaFiscalServico, Long> {

    List<NotaFiscalServico> findByCompetenciaBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<NotaFiscalServico> findByPrestadorCnpjCpf(String cnpjCpf);

    List<NotaFiscalServico> findByTomadorCnpjCpf(String cnpjCpf);

    Optional<NotaFiscalServico> findByNumeroNfse(Integer numeroNfse);
}
