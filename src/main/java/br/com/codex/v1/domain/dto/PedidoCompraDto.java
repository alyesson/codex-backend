package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.PedidoCompra;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class PedidoCompraDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Integer numeroCotacao;
    protected String solicitante;
    protected Date dataSolicitacao;
    protected Date dataAbertura;
    protected String situacao;
    protected String comprador;
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String cep;
    protected String contato;
    protected BigDecimal valorPedido;
    protected List<PedidoItensCompraDto> itens;

    public PedidoCompraDto() {
        super();
    }

    public PedidoCompraDto(PedidoCompra obj) {
        this.id = obj.getId();
        this.numeroCotacao = obj.getNumeroCotacao();
        this.solicitante = obj.getSolicitante();
        this.dataSolicitacao = obj.getDataSolicitacao();
        this.dataAbertura = obj.getDataAbertura();
        this.situacao = obj.getSituacao();
        this.comprador = obj.getComprador();
        this.fornecedor = obj.getFornecedor();
        this.cnpj = obj.getCnpj();
        this.ie = obj.getIe();
        this.endereco = obj.getEndereco();
        this.cep = obj.getCep();
        this.contato = obj.getContato();
        this.valorPedido = obj.getValorPedido();
    }
}
