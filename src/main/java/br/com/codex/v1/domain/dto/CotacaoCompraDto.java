package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CotacaoCompraDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected Integer numeroSolicitacao;
    protected String solicitante;
    protected LocalDate dataAbertura;
    @NotNull(message = "A validade da cotação não pode estar em branco")
    protected LocalDate validade;
    protected String situacao;
    protected String comprador;
    @NotBlank(message = "O fornecedor não pode estar em branco")
    protected String fornecedor;
    protected String cnpj;
    protected String ie;
    protected String endereco;
    @NotBlank(message = "O prazo de entrega não pode estar em branco")
    protected String prazoEntrega;
    @NotBlank(message = "O contato não pode estar em branco")
    protected String contato;
    @NotNull(message = "O valor cotado não pode estar em branco")
    protected BigDecimal valorCotado;
    protected String linkCompra;
    @NotBlank(message = "A condição de pagamento não pode estar em branco")
    protected String condicoesPagamento;
    protected String observacao;
    protected String justificativa;
    protected List<CotacaoItensCompraDto> itens;

    public CotacaoCompraDto() {
        super();
    }

    public CotacaoCompraDto(CotacaoCompra obj) {
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
}
