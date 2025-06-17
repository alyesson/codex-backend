package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracaoCertificadoRepository extends JpaRepository<ConfiguracaoCertificado, Long> {

    Optional<ConfiguracaoCertificado> findByCnpj(String cpfCnpj);

}
