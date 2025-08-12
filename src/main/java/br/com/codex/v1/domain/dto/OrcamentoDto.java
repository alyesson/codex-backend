package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class OrcamentoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;
    private String codigo;
    private String consumidor;
    private String documentoConsumidor;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;
    private String vendedor;
    private String tipoOrcamento;
    private String formaPagamento;
    private Situacao situacao;
    private BigDecimal valorFrete = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private BigDecimal descontoTotal = BigDecimal.ZERO;
    private BigDecimal valorFinal = BigDecimal.ZERO;
    private String observacoes;
    private List<OrcamentoItens> itens;

    public OrcamentoDto() {
        super();
    }

    public OrcamentoDto(Orcamento obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.consumidor = obj.getConsumidor();
        this.documentoConsumidor = obj.getDocumentoConsumidor();
        this.dataEmissao = obj.getDataEmissao();
        this.dataValidade = obj.getDataValidade();
        this.vendedor = obj.getVendedor();
        this.tipoOrcamento = obj.getTipoOrcamento();
        this.formaPagamento = obj.getFormaPagamento();
        this.situacao = obj.getSituacao();
        this.valorFrete = obj.getValorFrete();
        this.valorTotal = obj.getValorTotal();
        this.descontoTotal = obj.getDescontoTotal();
        this.valorFinal = obj.getValorFinal();
        this.observacoes = obj.getObservacoes();
        this.itens = obj.getItens();
    }
}
