package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.VendaDto;
import br.com.codex.v1.domain.enums.Situacao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Venda implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String consumidor;

    @Column(nullable = false)
    private String documentoConsumidor;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false, length = 30)
    private String vendedor;

    @Column(nullable = false, length = 30)
    private String tipoVenda;

    @Column(nullable = false, length = 30)
    private String formaPagamento;

    @Column(nullable = false, length = 30)
    private Situacao situacao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFrete = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descontoTotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorFinal = BigDecimal.ZERO;

    @Column(length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaItens> itens;

    public Venda() {
        super();
    }

    public Venda(Long id, String codigo, String consumidor, String documentoConsumidor, LocalDate dataEmissao, LocalDate dataValidade, String vendedor,
                 String tipoVenda, String formaPagamento, Situacao situacao, BigDecimal valorFrete,
                 BigDecimal valorTotal, BigDecimal descontoTotal, BigDecimal valorFinal,
                 String observacoes, List<VendaItens> itens) {
        this.id = id;
        this.codigo = codigo;
        this.consumidor = consumidor;
        this.documentoConsumidor = documentoConsumidor;
        this.dataEmissao = dataEmissao;
        this.dataValidade = dataValidade;
        this.vendedor = vendedor;
        this.tipoVenda = tipoVenda;
        this.formaPagamento = formaPagamento;
        this.situacao = situacao;
        this.valorFrete = valorFrete;
        this.valorTotal = valorTotal;
        this.descontoTotal = descontoTotal;
        this.valorFinal = valorFinal;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    public Venda(VendaDto obj) {
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
        this.valorTotal = obj.getValorTotal();
        this.descontoTotal = obj.getDescontoTotal();
        this.valorFinal = obj.getValorFinal();
        this.observacoes = obj.getObservacoes();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Venda orcamento = (Venda) o;
        return Objects.equals(id, orcamento.id) && Objects.equals(codigo, orcamento.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
