package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.contabilidade.Contas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContasRepository extends JpaRepository<Contas, Integer> {

    Optional<Contas> findByConta(String conta);

    Optional<Contas> findByReduzido(String reduzido);
}
