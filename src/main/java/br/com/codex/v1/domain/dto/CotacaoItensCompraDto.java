package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CotacaoItensCompraDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;
    protected BigDecimal frete;
    protected BigDecimal desconto;
    protected BigDecimal precoTotal;
    protected CotacaoCompraDto cotacaoCompra;

    public CotacaoItensCompraDto() {
        super();
    }

    public CotacaoItensCompraDto(CotacaoItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.precoUnitario = obj.getPrecoUnitario();
        this.desconto = obj.getDesconto();
        this.precoTotal = obj.getPrecoTotal();
        this.cotacaoCompra = new CotacaoCompraDto(obj.getCotacaoCompra());
    }
}
