package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

    Optional<ContaBancaria> findByNome(String nome);
}
