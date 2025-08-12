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


    protected Long id;
    @NotNull(message = "Código do produto não pode estar em branco")
    protected String codigoProduto;
    @NotNull(message = "Descrição do produto não pode estar em branco")
    protected String produto;
    protected Date dataEntrada;
    protected String fornecedor;
    protected String cnpjFornecedor;
    protected Integer quantidade;
    protected Integer notaFiscal;
    protected Date dataNota;
    protected BigDecimal valorProduto;
    protected BigDecimal valorNota;
    protected String lote;
    protected String validade;
    protected String autor;
    protected String motivoAcerto;
    protected String unidadeComercial;
    protected Integer quantidadePorUnidade;


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
    }
}
