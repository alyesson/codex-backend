package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.PedidoCompraDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PedidoCompra implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        protected Integer id;
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

    public PedidoCompra(Integer id, Integer numeroCotacao, String solicitante, Date dataSolicitacao, Date dataAbertura, String situacao, String comprador, String fornecedor, String cnpj, String ie, String endereco, String cep, String contato, BigDecimal valorPedido) {
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getNumeroCotacao() {
            return numeroCotacao;
        }

        public void setNumeroCotacao(Integer numeroCotacao) {
            this.numeroCotacao = numeroCotacao;
        }

        public String getSolicitante() {
            return solicitante;
        }

        public void setSolicitante(String solicitante) {
            this.solicitante = solicitante;
        }

        public Date getDataSolicitacao() {
            return dataSolicitacao;
        }

        public void setDataSolicitacao(Date dataSolicitacao) {
            this.dataSolicitacao = dataSolicitacao;
        }

        public Date getDataAbertura() {
            return dataAbertura;
        }

        public void setDataAbertura(Date dataAbertura) {
            this.dataAbertura = dataAbertura;
        }

        public String getSituacao() {
            return situacao;
        }

        public void setSituacao(String situacao) {
            this.situacao = situacao;
        }

        public String getComprador() {
            return comprador;
        }

        public void setComprador(String comprador) {
            this.comprador = comprador;
        }

        public String getFornecedor() {
            return fornecedor;
        }

        public void setFornecedor(String fornecedor) {
            this.fornecedor = fornecedor;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getIe() {
            return ie;
        }

        public void setIe(String ie) {
            this.ie = ie;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public String getContato() {
            return contato;
        }

        public void setContato(String contato) {
            this.contato = contato;
        }

    public BigDecimal getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(BigDecimal valorPedido) {
        this.valorPedido = valorPedido;
    }

    public List<PedidoItensCompra> getPedidoItensCompras() {
            return pedidoItensCompras;
        }

        public void setPedidoItensCompras(List<PedidoItensCompra> pedidoItensCompras) {
            this.pedidoItensCompras = pedidoItensCompras;
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
