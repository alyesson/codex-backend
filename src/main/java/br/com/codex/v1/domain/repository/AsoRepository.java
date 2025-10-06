package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.Aso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsoRepository extends JpaRepository<Aso, Long> {

}
