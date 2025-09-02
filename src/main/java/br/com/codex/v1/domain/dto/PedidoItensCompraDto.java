package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.PedidoItensCompra;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class PedidoItensCompraDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected String unidadeComercial;
    protected Integer quantidadePorUnidade;
    protected Integer quantidadeTotal;
    protected BigDecimal precoUnitario;
    protected BigDecimal ipi;
    protected BigDecimal pis;
    protected BigDecimal icms;
    protected BigDecimal cofins;
    protected BigDecimal desconto;
    protected BigDecimal frete;
    protected BigDecimal precoTotal;
    protected PedidoCompraDto pedidoCompra;

    public PedidoItensCompraDto() {
        super();
    }

    public PedidoItensCompraDto(PedidoItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.quantidadePorUnidade = obj.getQuantidadePorUnidade();
        this.quantidadeTotal = obj.getQuantidadeTotal();
        this.precoUnitario = obj.getPrecoUnitario();
        this.ipi = obj.getIpi();
        this.pis = obj.getPis();
        this.icms = obj.getIcms();
        this.cofins = obj.getCofins();
        this.desconto = obj.getDesconto();
        this.frete = obj.getFrete();
        this.precoTotal = obj.getPrecoTotal();
        this.pedidoCompra = new PedidoCompraDto(obj.getPedidoCompra());
    }
}
