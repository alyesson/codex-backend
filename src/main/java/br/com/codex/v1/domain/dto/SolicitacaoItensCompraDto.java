package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class SolicitacaoItensCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected SolicitacaoCompraDto solicitacaoCompra;
    protected String unidadeComercial;
    protected BigDecimal precoUnitario;

    public SolicitacaoItensCompraDto() {
        super();
    }

    public SolicitacaoItensCompraDto(SolicitacaoItensCompra obj) {
        this.id = obj.getId();
        this.codigoProduto = obj.getCodigoProduto();
        this.descricaoProduto = obj.getDescricaoProduto();
        this.quantidade = obj.getQuantidade();
        this.solicitacaoCompra = new SolicitacaoCompraDto(obj.getSolicitacaoCompra());
        this.unidadeComercial = obj.getUnidadeComercial();
        this.precoUnitario = obj.getPrecoUnitario();
    }
}
