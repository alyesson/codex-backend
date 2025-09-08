package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.ConfiguracaoContabil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfiguracaoContabilRepository extends JpaRepository<ConfiguracaoContabil, Long> {

    List<ConfiguracaoContabil> findByEmpresaId(Long empresaId);

    List<ConfiguracaoContabil> findByTipo(String tipo);

    List<ConfiguracaoContabil> findByEmpresaIdAndTipo(Long empresaId, String tipo);
}
