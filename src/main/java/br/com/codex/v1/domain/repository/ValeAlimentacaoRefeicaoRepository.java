package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.ValeAlimentacaoRefeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValeAlimentacaoRefeicaoRepository extends JpaRepository<ValeAlimentacaoRefeicao, Long> {

    // Buscar por colaborador
    List<ValeAlimentacaoRefeicao> findByColaboradorId(Long colaboradorId);

    // Buscar ativos
    List<ValeAlimentacaoRefeicao> findByAtivoTrue();

    // Buscar inativos
    List<ValeAlimentacaoRefeicao> findByAtivoFalse();

    // Buscar por tipo de benefício
    List<ValeAlimentacaoRefeicao> findByTipoBeneficio(String tipoBeneficio);

    // Buscar por forma de utilização
    List<ValeAlimentacaoRefeicao> findByFormaUtilizacao(String formaUtilizacao);

    // Buscar por bandeira do cartão
    List<ValeAlimentacaoRefeicao> findByBandeiraCartao(String bandeiraCartao);

    // Buscar por período de data início
    List<ValeAlimentacaoRefeicao> findByDataInicioBetween(LocalDate dataInicio, LocalDate dataFim);

    // Buscar colaboradores que usam refeitório interno
    List<ValeAlimentacaoRefeicao> findByRefeitorioInternoTrue();

    // Buscar colaboradores que usam restaurantes conveniados
    List<ValeAlimentacaoRefeicao> findByRestauranteConveniadoTrue();

    // Buscar vale ativo por colaborador
    @Query("SELECT va FROM ValeAlimentacaoRefeicao va WHERE va.colaborador.id = :colaboradorId AND va.ativo = true")
    Optional<ValeAlimentacaoRefeicao> findAtivoByColaboradorId(@Param("colaboradorId") Long colaboradorId);

    // Buscar vale ativo por CPF
    @Query("SELECT va FROM ValeAlimentacaoRefeicao va WHERE va.colaborador.cpf = :cpf AND va.ativo = true")
    Optional<ValeAlimentacaoRefeicao> findAtivoByColaboradorCpf(@Param("cpf") String cpf);

    // Buscar vencimentos próximos (30 dias)
    @Query("SELECT va FROM ValeAlimentacaoRefeicao va WHERE va.dataVencimentoCartao BETWEEN :hoje AND :dataLimite")
    List<ValeAlimentacaoRefeicao> findVencimentosProximos(@Param("hoje") LocalDate hoje,
                                                          @Param("dataLimite") LocalDate dataLimite);

    // Buscar com JOIN FETCH para evitar Lazy Loading
    @Query("SELECT va FROM ValeAlimentacaoRefeicao va JOIN FETCH va.colaborador")
    List<ValeAlimentacaoRefeicao> findAllWithColaborador();

    // Estatísticas - total de benefícios ativos por tipo
    @Query("SELECT va.tipoBeneficio, COUNT(va) FROM ValeAlimentacaoRefeicao va WHERE va.ativo = true GROUP BY va.tipoBeneficio")
    List<Object[]> countAtivosPorTipo();
}
