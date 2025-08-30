package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.OrdemItensCompra;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OrdemItensCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected OrdemCompraDto solicitacaoCompra;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;

    public OrdemItensCompraDto() {
        super();
    }

    public OrdemItensCompraDto(OrdemItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.solicitacaoCompra = new OrdemCompraDto(obj.getOrdemCompra());
        this.unidadeComercial = obj.getUnidadeComercial();
        this.precoUnitario = obj.getPrecoUnitario();
    }
}
