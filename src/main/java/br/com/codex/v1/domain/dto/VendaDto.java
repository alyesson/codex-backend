package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.vendas.Venda;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class VendaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Date dataVenda;
    protected String cliente;
    protected String cpf;
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
        this.cpf = objVenda.getCpf();
        this.totalVenda = objVenda.getTotalVenda();
        this.formaPagamento = objVenda.getFormaPagamento();
        this.status = objVenda.getStatus();
        this.vendedor = objVenda.getVendedor();
        this.quantidade = objVenda.getQuantidade();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}