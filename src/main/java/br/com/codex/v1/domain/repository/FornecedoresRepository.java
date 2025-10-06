package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.Fornecedores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedoresRepository extends JpaRepository<Fornecedores, Long> {
    Optional<Fornecedores> findByCnpj(String cnpj);
}
