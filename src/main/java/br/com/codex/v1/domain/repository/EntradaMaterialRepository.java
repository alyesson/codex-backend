package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntradaMaterialRepository extends JpaRepository<EntradaMaterial, Long> {

    boolean existsByNotaFiscalAndCodigoProduto(Integer notaFiscal,String codigoProduto);

    Optional<EntradaMaterial> findByLote(String lote);

    @Query(value = "SELECT p FROM EntradaMaterial p WHERE p.codigoProduto=:codigoProduto and p.lote=:lote")
    Optional<EntradaMaterial> findByCodigoProdutoAndLote(@Param("codigoProduto") String codigoProduto, @Param("lote") String lote);

    @Query(value = "SELECT p FROM EntradaMaterial p WHERE p.quantidade!= 0")
    List<EntradaMaterial> findAllByProdutoEstoque();

    @Query("SELECT a FROM EntradaMaterial a WHERE a.dataEntrada BETWEEN :dataInicial AND :dataFinal")
    List<EntradaMaterial> findAllEntradaPeriodo(@Param("dataInicial") Date dataInicial, @Param("dataFinal") Date dataFinal);

    Optional<EntradaMaterial> findByCodigoProduto(String codigoProduto);
}
