package br.com.codex.v1.domain.dto;


import br.com.codex.v1.domain.vendas.VendaItens;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class VendaItensDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String cpfCnpj;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected BigDecimal valorProduto;
    protected BigDecimal valorTotal;

    public VendaItensDto() {
        super();
    }

    public VendaItensDto(VendaItens objItens) {
        this.id = objItens.getId();
        this.cpfCnpj = objItens.getCpfCnpj();
        this.codigoProduto = objItens.getCodigoProduto();
        this.descricaoProduto = objItens.getDescricaoProduto();
        this.quantidade = objItens.getQuantidade();
        this.valorProduto = objItens.getValorProduto();
        this.valorTotal = objItens.getValorTotal();
    }
}