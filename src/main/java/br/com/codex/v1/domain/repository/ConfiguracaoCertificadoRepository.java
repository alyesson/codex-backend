package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoCertificadoRepository extends JpaRepository<ConfiguracaoCertificado, Long> {

    Optional<ConfiguracaoCertificado> findByCnpj(String cpfCnpj);

}
