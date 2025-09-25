package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.EntradaMaterialDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
public class EntradaMaterial implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigoProduto;
    private String produto;
    private Date dataEntrada;
    private String fornecedor;
    private String cnpjFornecedor;
    private Integer quantidade;
    private Integer notaFiscal;
    private Date dataNota;
    private BigDecimal valorProduto;
    private BigDecimal valorNota;
    private String lote;
    private String validade;
    private String autor;
    private String motivoAcerto;
    private String unidadeComercial;
    private Integer quantidadePorUnidade;
    private String local;

    public EntradaMaterial() {
        super();
    }

    public EntradaMaterial(Long id, String codigoProduto, String produto, Date dataEntrada, String fornecedor,
                           String cnpjFornecedor, Integer quantidade, Integer notaFiscal, Date dataNota,
                           BigDecimal valorProduto, BigDecimal valorNota, String lote, String validade,
                           String autor, String motivoAcerto, String unidadeComercial, Integer quantidadePorUnidade,
                           String local) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.produto = produto;
        this.dataEntrada = dataEntrada;
        this.fornecedor = fornecedor;
        this.cnpjFornecedor = cnpjFornecedor;
        this.quantidade = quantidade;
        this.notaFiscal = notaFiscal;
        this.dataNota = dataNota;
        this.valorProduto = valorProduto;
        this.valorNota = valorNota;
        this.lote = lote;
        this.validade = validade;
        this.autor = autor;
        this.motivoAcerto = motivoAcerto;
        this.unidadeComercial = unidadeComercial;
        this.quantidadePorUnidade = quantidadePorUnidade;
        this.local = local;
    }

    public EntradaMaterial(EntradaMaterialDto obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.produto = obj.getProduto();
        this.dataEntrada = obj.getDataEntrada();
        this.fornecedor = obj.getFornecedor();
        this.cnpjFornecedor = obj.getCnpjFornecedor();
        this.quantidade = obj.getQuantidade();
        this.notaFiscal = obj.getNotaFiscal();
        this.dataNota = obj.getDataNota();
        this.valorProduto = obj.getValorProduto();
        this.valorNota = obj.getValorNota();
        this.lote = obj.getLote();
        this.validade = obj.getValidade();
        this.autor = obj.getAutor();
        this.motivoAcerto = obj.getMotivoAcerto();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.quantidadePorUnidade = obj.getQuantidadePorUnidade();
        this.local = obj.getLocal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntradaMaterial that = (EntradaMaterial) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
