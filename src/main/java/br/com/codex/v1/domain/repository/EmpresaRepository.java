package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
