package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroJornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CadastroJornadaRepository extends JpaRepository<CadastroJornada, Long> {

    @Query("SELECT j FROM CadastroJornada j WHERE j.codigoJornada = :codigoJornada")
    List<CadastroJornada> findByCodigoJornada(@Param("codigoJornada") String codigoJornada);

    // NOVO MÉTUDO - Retorna apenas uma jornada (pega a primeira da lista)
    @Query("SELECT j FROM CadastroJornada j WHERE j.codigoJornada = :codigoJornada")
    CadastroJornada findFirstByCodigoJornada(@Param("codigoJornada") String codigoJornada);

    @Transactional
    void deleteByCodigoJornada(String codigoJornada);
}
