package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class OrcamentoItensDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private OrcamentoDto orcamentoId; // ou String codigoOrcamento
    private String codigo;
    private String descricao;
    private BigDecimal quantidade = BigDecimal.ONE;
    private BigDecimal valorUnitario = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public OrcamentoItensDto() {
        super();
    }

    public OrcamentoItensDto(OrcamentoItens obj) {
        this.id = obj.getId();
        this.orcamentoId = new OrcamentoDto(obj.getOrcamento());
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.quantidade = obj.getQuantidade();
        this.valorUnitario = obj.getValorUnitario();
        this.valorTotal = obj.getValorTotal();
    }
}
