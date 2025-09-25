package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class EntradaMaterialDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;
    @NotNull(message = "Código do produto não pode estar em branco")
    private String codigoProduto;
    @NotNull(message = "Descrição do produto não pode estar em branco")
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


    public EntradaMaterialDto() {
        super();
    }

    public EntradaMaterialDto(EntradaMaterial obj) {
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
}
