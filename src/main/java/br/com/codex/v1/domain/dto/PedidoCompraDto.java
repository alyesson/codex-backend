package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.PedidoCompra;
import br.com.codex.v1.domain.enums.Situacao;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PedidoCompraDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Integer numeroCotacao;
    protected String numeroRequisicao;
    protected String solicitante;
    protected LocalDate dataPedido;
    protected LocalDate dataAprovacao;
    protected LocalDate dataEntregaPrevista;
    protected LocalDate dataEntregaReal;
    protected LocalDate validade;
    protected String aprovador;
    protected String departamento;
    protected String centroCusto;
    protected Situacao situacao;
    protected String comprador;
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String contato;
    protected BigDecimal valorPedido;
    protected BigDecimal valorFrete;
    protected BigDecimal valorDesconto;
    protected BigDecimal valorTotal;
    protected String linkCompra;
    protected String condicoesPagamento;
    protected String formaPagamento;
    protected Integer numeroParcelas;
    protected String observacao;
    protected String justificativa;
    protected List<PedidoItensCompraDto> itens;

    public PedidoCompraDto() {
        super();
    }

    public PedidoCompraDto(PedidoCompra obj) {
        this.id = obj.getId();
        this.numeroCotacao = obj.getNumeroCotacao();
        this.numeroRequisicao = obj.getNumeroRequisicao();
        this.solicitante = obj.getSolicitante();
        this.dataPedido = obj.getDataPedido();
        this.dataAprovacao = obj.getDataAprovacao();
        this.dataEntregaPrevista = obj.getDataEntregaPrevista();
        this.dataEntregaReal = obj.getDataEntregaReal();
        this.validade = obj.getValidade();
        this.aprovador = obj.getAprovador();
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
        this.situacao = obj.getSituacao();
        this.comprador = obj.getComprador();
        this.fornecedor = obj.getFornecedor();
        this.cnpj = obj.getCnpj();
        this.ie = obj.getIe();
        this.endereco = obj.getEndereco();
        this.contato = obj.getContato();
        this.valorPedido = obj.getValorPedido();
        this.valorFrete = obj.getValorFrete();
        this.valorDesconto = obj.getValorDesconto();
        this.valorTotal = obj.getValorTotal();
        this.linkCompra = obj.getLinkCompra();
        this.condicoesPagamento = obj.getCondicoesPagamento();
        this.formaPagamento = obj.getFormaPagamento();
        this.numeroParcelas = obj.getNumeroParcelas();
        this.observacao = obj.getObservacao();
        this.justificativa = obj.getJustificativa();
    }
}
