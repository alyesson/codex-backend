package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.EmissorBoleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmissorBoletoRepository extends JpaRepository<EmissorBoleto, Long> {

    Optional<EmissorBoleto> findByDocumentoSacado(String documento);

    @Query("SELECT b FROM EmissorBoleto b WHERE b.dataVencimento BETWEEN :dataInicial AND :dataFinal")
    List<EmissorBoleto> findAllBoletosPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal);
}
