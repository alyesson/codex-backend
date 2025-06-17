package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroJornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CadastroJornadaRepository extends JpaRepository<CadastroJornada, Long> {

    @Query("SELECT j FROM CadastroJornada j WHERE j.codigoJornada = :codigoJornada")
    List<CadastroJornada> findByCodigoJornada(@Param("codigoJornada") String codigoJornada);

    @Transactional
    void deleteByCodigoJornada(String codigoJornada);
}
