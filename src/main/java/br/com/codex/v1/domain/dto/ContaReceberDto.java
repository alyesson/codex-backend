package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.ContaReceber;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.MinimizarPalavras.minimizarPalavras;

@Getter
@Setter
public class ContaReceberDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String descricao;
    protected String categoria;
    protected String recebidoDe;
    protected String numeroDocumento;
    protected String repete;
    protected Date dataVencimento;
    protected Date dataCompetencia;
    protected Date dataEmissao;
    protected Integer quantidadeParcelas;
    protected BigDecimal valor;
    protected String metodoRecebimento;
    protected String situacao;
    protected String observacao;
    protected String origemDocumento;

    public ContaReceberDto() {
        super();
    }

    public ContaReceberDto(ContaReceber obj) {
        this.id = obj.getId();
        this.descricao = obj.getDescricao();
        this.categoria = obj.getCategoria();
        this.recebidoDe = obj.getRecebidoDe();
        this.numeroDocumento = obj.getNumeroDocumento();
        this.repete = obj.getRepete();
        this.dataVencimento = obj.getDataVencimento();
        this.dataCompetencia = obj.getDataCompetencia();
        this.dataEmissao = obj.getDataEmissao();
        this.quantidadeParcelas = obj.getQuantidadeParcelas();
        this.valor = obj.getValor();
        this.metodoRecebimento = obj.getMetodoRecebimento();
        this.situacao = obj.getSituacao();
        this.observacao = obj.getObservacao();
        this.origemDocumento = obj.getOrigemDocumento();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public void setObservacao(String observacao) {
        this.observacao = minimizarPalavras(observacao);
    }

}
