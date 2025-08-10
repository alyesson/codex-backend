package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.VendaDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Venda implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Date dataVenda;
    protected String cliente;
    protected String cpfCnpj;
    protected BigDecimal totalVenda;
    protected String formaPagamento;
    protected String status;
    protected String vendedor;
    protected Integer quantidade;

    public Venda() {
        super();
    }

    public Venda(Long id, Date dataVenda, String cliente, String cpfCnpj, BigDecimal totalVenda, String formaPagamento, String status, String vendedor, Integer quantidade) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.cliente = cliente;
        this.cpfCnpj = cpfCnpj;
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
        this.cpfCnpj = objVenda.getCpfCnpj();
        this.totalVenda = objVenda.getTotalVenda();
        this.formaPagamento = objVenda.getFormaPagamento();
        this.status = objVenda.getStatus();
        this.vendedor = objVenda.getVendedor();
        this.quantidade = objVenda.getQuantidade();
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
