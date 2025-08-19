package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Venda;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VendaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String consumidor;
    private String documentoConsumidor;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;
    private String vendedor;
    private String tipoVenda;
    private String formaPagamento;
    private Situacao situacao;
    private BigDecimal valorFrete = BigDecimal.ZERO;
    private BigDecimal descontoTotal = BigDecimal.ZERO;
    private BigDecimal valorFinal = BigDecimal.ZERO;
    private String observacoes;
    private List<VendaItensDto> itens;

    public VendaDto() {
        super();
    }

    public VendaDto(Venda obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.consumidor = obj.getConsumidor();
        this.documentoConsumidor = obj.getDocumentoConsumidor();
        this.dataEmissao = obj.getDataEmissao();
        this.dataValidade = obj.getDataValidade();
        this.vendedor = obj.getVendedor();
        this.tipoVenda = obj.getTipoVenda();
        this.formaPagamento = obj.getFormaPagamento();
        this.situacao = obj.getSituacao();
        this.valorFrete = obj.getValorFrete();
        this.descontoTotal = obj.getDescontoTotal();
        this.valorFinal = obj.getValorFinal();
        this.observacoes = obj.getObservacoes();
    }
}
