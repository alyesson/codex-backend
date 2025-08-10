package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.vendas.Venda;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class VendaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Date dataVenda;
    protected String cliente;
    protected String cpfCnpj;
    protected BigDecimal totalVenda;
    protected String formaPagamento;
    protected String status;
    protected String vendedor;
    protected Integer quantidade;

    public VendaDto() {
        super();
    }

    public VendaDto(Venda objVenda) {
        this.id = objVenda.getId();
        this.dataVenda = objVenda.getDataVenda();
        this.cliente = objVenda.getCliente();
        this.cpfCnpj = objVenda.getCpfCnpj();
        this.totalVenda = objVenda.getTotalVenda();
        this.formaPagamento = objVenda.getFormaPagamento();
        this.status = objVenda.getStatus();
        this.vendedor = objVenda.getVendedor();
        this.quantidade = objVenda.getQuantidade();
    }
}