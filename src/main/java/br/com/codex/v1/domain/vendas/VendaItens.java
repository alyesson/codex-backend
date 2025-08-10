package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.VendaItensDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class VendaItens implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String cpfCnpj;
    protected String codigoProduto;
    protected String descricaoProduto;
    protected Integer quantidade;
    protected BigDecimal valorProduto;
    protected BigDecimal valorTotal;

    public VendaItens() {
        super();
    }

    public VendaItens(Long id, String cpfCnpj, String codigoProduto, String descricaoProduto, Integer quantidade, BigDecimal valorProduto, BigDecimal valorTotal) {
        this.id = id;
        this.cpfCnpj = cpfCnpj;
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.quantidade = quantidade;
        this.valorProduto = valorProduto;
        this.valorTotal = valorTotal;
    }

    public VendaItens(VendaItensDto objItens) {
        this.id = objItens.getId();
        this.cpfCnpj = objItens.getCpfCnpj();
        this.codigoProduto = objItens.getCodigoProduto();
        this.descricaoProduto = objItens.getDescricaoProduto();
        this.quantidade = objItens.getQuantidade();
        this.valorProduto = objItens.getValorProduto();
        this.valorTotal = objItens.getValorTotal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendaItens that = (VendaItens) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
