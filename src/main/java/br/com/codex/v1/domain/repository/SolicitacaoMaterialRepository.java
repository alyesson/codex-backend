package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SolicitacaoMaterialRepository extends JpaRepository<SolicitacaoMaterial, Long> {

    Optional<SolicitacaoMaterial> findBySituacao(Integer codigo);

    //Lista data solicitacao por período
    @Query("SELECT s FROM SolicitacaoMaterial s WHERE s.dataSolicitacao BETWEEN (dataSolicitacao) and s.dataSolicitacao =:dataSolicitacao")
    List<SolicitacaoMaterial> findByDataSolicitacao(@Param("dataSolicitacao")LocalDate dataInicio, @Param("dataSolicitacao")LocalDate dataFim);

    //Lista data solicitacao do ano corrente
    @Query("SELECT s FROM SolicitacaoMaterial s WHERE s.dataSolicitacao YEAR(dataSolicitacao) =:dataSolicitacao")
    List<SolicitacaoMaterial> findByDataSolicitacaoYear(@Param("dataSolicitacao")LocalDate ano);

    //Lista data solicitacao do ano corrente do usuário
    @Query("SELECT s FROM SolicitacaoMaterial s WHERE s.dataSolicitacao YEAR(dataSolicitacao) =:dataSolicitacao and s.email =:email")
    List<SolicitacaoMaterial> findByDataSolicitacaoAndEmail(@Param("dataSolicitacao")LocalDate ano, @Param("email")String email);
}
