package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.PedidoCompraDto;
import br.com.codex.v1.domain.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class PedidoCompra implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        @JsonIgnore
        @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
        protected List<PedidoItensCompra> itens = new ArrayList<>();

    public PedidoCompra() {
            super();
        }

    public PedidoCompra(Long id, Integer numeroCotacao, String numeroRequisicao, String solicitante,
                        LocalDate dataPedido, LocalDate dataAprovacao, LocalDate dataEntregaPrevista,
                        LocalDate dataEntregaReal, LocalDate validade, String aprovador, String departamento,
                        String centroCusto, Situacao situacao, String comprador, String fornecedor, String cnpj,
                        String ie, String endereco, String contato, BigDecimal valorPedido, BigDecimal valorFrete,
                        BigDecimal valorDesconto, BigDecimal valorTotal, String linkCompra, String condicoesPagamento,
                        String formaPagamento, Integer numeroParcelas, String observacao, String justificativa) {
        this.id = id;
        this.numeroCotacao = numeroCotacao;
        this.numeroRequisicao = numeroRequisicao;
        this.solicitante = solicitante;
        this.dataPedido = dataPedido;
        this.dataAprovacao = dataAprovacao;
        this.dataEntregaPrevista = dataEntregaPrevista;
        this.dataEntregaReal = dataEntregaReal;
        this.validade = validade;
        this.aprovador = aprovador;
        this.departamento = departamento;
        this.centroCusto = centroCusto;
        this.situacao = situacao;
        this.comprador = comprador;
        this.fornecedor = fornecedor;
        this.cnpj = cnpj;
        this.ie = ie;
        this.endereco = endereco;
        this.contato = contato;
        this.valorPedido = valorPedido;
        this.valorFrete = valorFrete;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
        this.linkCompra = linkCompra;
        this.condicoesPagamento = condicoesPagamento;
        this.formaPagamento = formaPagamento;
        this.numeroParcelas = numeroParcelas;
        this.observacao = observacao;
        this.justificativa = justificativa;
    }

    public PedidoCompra(PedidoCompraDto obj) {
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

    @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PedidoCompra that = (PedidoCompra) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
}
