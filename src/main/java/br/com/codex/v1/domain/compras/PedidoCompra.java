package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.PedidoCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
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
        @JsonIgnore
        @OneToMany(mappedBy = "pedidoCompra")
        protected List<PedidoItensCompra> pedidoItensCompras = new ArrayList<>();

    public PedidoCompra() {
            super();
        }

    public PedidoCompra(Long id, Integer numeroCotacao, String solicitante, Date dataSolicitacao, Date dataAbertura, String situacao, String comprador, String fornecedor, String cnpj, String ie, String endereco, String cep, String contato, BigDecimal valorPedido) {
            this.id = id;
            this.numeroCotacao = numeroCotacao;
            this.solicitante = solicitante;
            this.dataSolicitacao = dataSolicitacao;
            this.dataAbertura = dataAbertura;
            this.situacao = situacao;
            this.comprador = comprador;
            this.fornecedor = fornecedor;
            this.cnpj = cnpj;
            this.ie = ie;
            this.endereco = endereco;
            this.cep = cep;
            this.contato = contato;
            this.valorPedido = valorPedido;
        }

    public PedidoCompra(PedidoCompraDto obj) {
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
