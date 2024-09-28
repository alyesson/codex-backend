package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.financeiro.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Integer> {

    Optional<ContaBancaria> findByNome(String nome);
}
