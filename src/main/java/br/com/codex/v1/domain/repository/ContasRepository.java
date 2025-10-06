package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.Contas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContasRepository extends JpaRepository<Contas, Long> {

    //Optional<Contas> findByConta(String conta);
    Optional<Contas> findByReduzido(String reduzido);
    Optional<Contas> findByNome(String nome);
    //Aqui ele procura o nome e se não houver correspondência exata,
    // ele busca por nomes que contenham a string e usa o primeiro da lista, evitando o erro por múltiplos resultados.
    List<Contas> findByNomeIgnoreCaseContaining(String nome);

    Contas findByConta(String conta);
}
