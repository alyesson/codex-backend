package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TabelaDeducaoInssRepository extends JpaRepository<TabelaDeducaoInss, Long> {

    Optional<TabelaDeducaoInss> findTopByOrderById();
}
