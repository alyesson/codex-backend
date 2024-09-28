package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.compras.Fornecedores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedoresRepository extends JpaRepository<Fornecedores, Integer> {
    Optional<Fornecedores> findByCnpj(String cnpj);
}
