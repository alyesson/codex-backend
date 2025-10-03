package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.ValeTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ValeTransporteRepository extends JpaRepository<ValeTransporte, Long> {

    List<ValeTransporte> findByColaboradorId(Long colaboradorId);

    List<ValeTransporte> findByAtivoTrue();

    List<ValeTransporte> findByAtivoFalse();

    List<ValeTransporte> findByDataInicioBetween(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT vt FROM ValeTransporte vt WHERE vt.colaborador.cpf = :cpf AND vt.ativo = true")
    Optional<ValeTransporte> findByColaboradorCpfAndAtivo(@Param("cpf") String cpf);

    @Query("SELECT vt FROM ValeTransporte vt WHERE vt.colaborador.id = :colaboradorId AND vt.ativo = true")
    Optional<ValeTransporte> findAtivoByColaboradorId(@Param("colaboradorId") Long colaboradorId);

    List<ValeTransporte> findByTipoVale(String tipoVale);
}