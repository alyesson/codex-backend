package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.vendas.OrcamentoItens;
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

    private Long id;
    private OrcamentoDto orcamentoId; // ou String codigoOrcamento
    private String codigo;
    private String descricao;
    private BigDecimal quantidade = BigDecimal.ONE;
    private BigDecimal valorUnitario = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public VendaItensDto() {
        super();
    }

    public VendaItensDto(OrcamentoItens obj) {
        this.id = obj.getId();
        this.orcamentoId = new OrcamentoDto(obj.getOrcamento());
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.quantidade = obj.getQuantidade();
        this.valorUnitario = obj.getValorUnitario();
        this.desconto = obj.getDesconto();
        this.valorTotal = obj.getValorTotal();
    }
}
