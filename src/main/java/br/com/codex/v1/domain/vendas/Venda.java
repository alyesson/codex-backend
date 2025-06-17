package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.VendaDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Venda implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Date dataVenda;
    protected String cliente;
    protected String cpf;
    protected BigDecimal totalVenda;
    protected String formaPagamento;
    protected String status;
    protected String vendedor;
    protected Integer quantidade;

    public Venda() {
        super();
    }

    public Venda(Long id, Date dataVenda, String cliente, String cpf, BigDecimal totalVenda, String formaPagamento, String status, String vendedor, Integer quantidade) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.cliente = cliente;
        this.cpf = cpf;
        this.totalVenda = totalVenda;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.vendedor = vendedor;
        this.quantidade = quantidade;
    }

    public Venda(VendaDto objVenda) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda vendas = (Venda) o;
        return Objects.equals(id, vendas.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
