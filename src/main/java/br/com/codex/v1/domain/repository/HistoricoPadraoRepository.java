package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoPadraoRepository extends JpaRepository<HistoricoPadrao, Long> {

    Optional<HistoricoPadrao> findByCodigo(String codigo);
    Optional<HistoricoPadrao> findByDescricao(String descricao);
    //Aqui ele procura o nome e se não houver correspondência exata,
    // ele busca por nomes que contenham a string e usa o primeiro da lista, evitando o erro por múltiplos resultados.
    List<HistoricoPadrao> findByDescricaoIgnoreCaseContaining(String descricao);
}
