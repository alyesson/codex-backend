package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroColaboradores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CadastroColaboradoresRepository extends JpaRepository<CadastroColaboradores, Long> {

    Optional<CadastroColaboradores> findByCpf(String cpf);

    @Query("SELECT c.situacaoAtual, COUNT(c) FROM CadastroColaboradores c WHERE c.situacaoAtual != 'Desligado' GROUP BY c.situacaoAtual")
    List<Object[]> countBySituacaoAtualAgrupado();

    @Query("SELECT c FROM CadastroColaboradores c WHERE c.situacaoAtual != 'Desligado'")
    List<CadastroColaboradores> findAllColaboradoresComSituacaoContratado();

    @Query("SELECT c FROM CadastroColaboradores c WHERE c.situacaoAtual = 'Desligado'")
    List<CadastroColaboradores> findAllColaboradoresComSituacaoDesligado();

    Optional<CadastroColaboradores> findByNomeColaborador(String nomeColaborador);

    @Query("SELECT c FROM CadastroColaboradores c WHERE c.numeroPis = :pis")
    CadastroColaboradores findByNumeroPis(@Param("pis") String pis);

    Optional<CadastroColaboradores> findByNumeroMatricula(String matricula);
}
