package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByCnpj(String cnpj);

    @Query("SELECT e FROM Empresa e WHERE e.possuiBase = true")
    List<Empresa> findAllByDataBase();

    @Query("SELECT e FROM Empresa e WHERE e.tipoEmpresa IN ('Matriz', 'Filial')")
    List<Empresa> findAllByTipoEmpresa();
}
