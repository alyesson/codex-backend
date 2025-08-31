package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CotacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Integer numeroSolicitacao;
    protected String solicitante;
    protected Date dataSolicitacao;
    protected Date dataAbertura;
    protected Date validade;
    protected String situacao;
    protected String comprador;
    @NotNull(message = "O fornecedor não pode estar em branco")
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
    protected List<CotacaoItensCompraDto> itens;

    public CotacaoCompraDto() {
        super();
    }

    public CotacaoCompraDto(CotacaoCompra obj) {
        this.id = obj.getId();
        this.numeroSolicitacao = obj.getNumeroSolicitacao();
        this.solicitante = obj.getSolicitante();
        this.dataSolicitacao = obj.getDataSolicitacao();
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
    }
}
