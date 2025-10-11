package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.CotacaoCompraDto;
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
public class CotacaoCompra implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Integer numeroSolicitacao;
    protected String solicitante;
    protected LocalDate dataAbertura;
    protected LocalDate validade;
    protected String situacao;
    protected String comprador;
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String prazoEntrega;
    protected String contato;
    protected BigDecimal valorCotado;
    protected String linkCompra;
    protected String condicoesPagamento;
    protected String observacao;
    protected String justificativa;
    @JsonIgnore
    @OneToMany(mappedBy = "cotacaoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<CotacaoItensCompra> cotacaoItensCompras = new ArrayList<>();

    public CotacaoCompra() {
        super();
    }

    public CotacaoCompra(Long id, Integer numeroSolicitacao, String solicitante, LocalDate dataAbertura,
                         LocalDate validade, String situacao, String comprador, String fornecedor, String cnpj,
                         String ie, String endereco, String prazoEntrega, String contato, BigDecimal valorCotado,
                         String linkCompra, String condicoesPagamento, String observacao, String justificativa) {
        this.id = id;
        this.numeroSolicitacao = numeroSolicitacao;
        this.solicitante = solicitante;
        this.dataAbertura = dataAbertura;
        this.validade = validade;
        this.situacao = situacao;
        this.comprador = comprador;
        this.fornecedor = fornecedor;
        this.cnpj = cnpj;
        this.ie = ie;
        this.endereco = endereco;
        this.prazoEntrega = prazoEntrega;
        this.contato = contato;
        this.valorCotado = valorCotado;
        this.linkCompra = linkCompra;
        this.condicoesPagamento = condicoesPagamento;
        this.observacao = observacao;
        this.justificativa = justificativa;
    }

    public CotacaoCompra(CotacaoCompraDto obj) {
        this.id = obj.getId();
        this.numeroSolicitacao = obj.getNumeroSolicitacao();
        this.solicitante = obj.getSolicitante();
        this.dataAbertura = obj.getDataAbertura();
        this.validade = obj.getValidade();
        this.situacao = obj.getSituacao();
        this.comprador = obj.getComprador();
        this.fornecedor = obj.getFornecedor();
        this.cnpj = obj.getCnpj();
        this.ie = obj.getIe();
        this.endereco = obj.getEndereco();
        this.prazoEntrega = obj.getPrazoEntrega();
        this.contato = obj.getContato();
        this.valorCotado = obj.getValorCotado();
        this.linkCompra = obj.getLinkCompra();
        this.condicoesPagamento = obj.getCondicoesPagamento();
        this.observacao = obj.getObservacao();
        this.justificativa = obj.getJustificativa();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CotacaoCompra that = (CotacaoCompra) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
