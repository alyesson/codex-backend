package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroColaboradores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CadastroColaboradoresRepository extends JpaRepository<CadastroColaboradores, Integer> {

    Optional<CadastroColaboradores> findByCpf(String cpf);

    @Query("SELECT c.situacaoAtual, COUNT(c) FROM CadastroColaboradores c WHERE c.situacaoAtual != 'Desligado' GROUP BY c.situacaoAtual")
    List<Object[]> countBySituacaoAtualAgrupado();

    @Query("SELECT c FROM CadastroColaboradores c WHERE c.situacaoAtual != 'Desligado'")
    List<CadastroColaboradores> findAllColaboradoresComSituacaoContratado();

    @Query("SELECT c FROM CadastroColaboradores c WHERE c.situacaoAtual = 'Desligado'")
    List<CadastroColaboradores> findAllColaboradoresComSituacaoDesligado();

    Optional<CadastroColaboradores> findByNomeColaborador(String nomeColaborador);
}
