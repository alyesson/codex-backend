package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Contas;
import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.estoque.NotasFiscais;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;


public class LancamentoContabilDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date dataLancamento;
    private BigDecimal valor;
    private Integer contaDebitoId;
    private String contaDebitoNome;
    private Integer contaCreditoId;
    private String contaCreditoNome;
    private Integer historicoPadraoId;
    private String historicoPadraoDescricao;
    private Integer notaFiscalOrigemId;
    private String notaFiscalNumero;
    private String complementoHistorico; // opcional, se quiser adicionar infos extras

    public LancamentoContabilDto() {
    }

    public LancamentoContabilDto(LancamentoContabil obj) {
        this.id = obj.getId();
        this.dataLancamento = obj.getDataLancamento();
        this.valor = obj.getValor();
        this.contaDebitoId = obj.getContaDebito() != null ? obj.getContaDebito().getId() : null;
        this.contaDebitoNome = obj.getContaDebito() != null ? obj.getContaDebito().getNome() : null;
        this.contaCreditoId = obj.getContaCredito() != null ? obj.getContaCredito().getId() : null;
        this.contaCreditoNome = obj.getContaCredito() != null ? obj.getContaCredito().getNome() : null;
        this.historicoPadraoId = obj.getHistoricoPadrao() != null ? obj.getHistoricoPadrao().getId() : null;
        this.historicoPadraoDescricao = obj.getHistoricoPadrao() != null ? obj.getHistoricoPadrao().getDescricao() : null;
        this.notaFiscalOrigemId = obj.getNotaFiscalOrigem() != null ? obj.getNotaFiscalOrigem().getId() : null;
        this.notaFiscalNumero = obj.getNotaFiscalOrigem() != null ? obj.getNotaFiscalOrigem().getNumero() : null;
        this.complementoHistorico = obj.getComplementoHistorico();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getContaDebitoId() {
        return contaDebitoId;
    }

    public void setContaDebitoId(Integer contaDebitoId) {
        this.contaDebitoId = contaDebitoId;
    }

    public Integer getContaCreditoId() {
        return contaCreditoId;
    }

    public void setContaCreditoId(Integer contaCreditoId) {
        this.contaCreditoId = contaCreditoId;
    }

    public Integer getHistoricoPadraoId() {
        return historicoPadraoId;
    }

    public void setHistoricoPadraoId(Integer historicoPadraoId) {
        this.historicoPadraoId = historicoPadraoId;
    }

    public String getHistoricoPadraoDescricao() {
        return historicoPadraoDescricao;
    }

    public void setHistoricoPadraoDescricao(String historicoPadraoDescricao) {
        this.historicoPadraoDescricao = historicoPadraoDescricao;
    }

    public Integer getNotaFiscalOrigemId() {
        return notaFiscalOrigemId;
    }

    public void setNotaFiscalOrigemId(Integer notaFiscalOrigemId) {
        this.notaFiscalOrigemId = notaFiscalOrigemId;
    }

    public String getNotaFiscalNumero() {
        return notaFiscalNumero;
    }

    public void setNotaFiscalNumero(String notaFiscalNumero) {
        this.notaFiscalNumero = notaFiscalNumero;
    }

    public String getComplementoHistorico() {
        return complementoHistorico;
    }

    public void setComplementoHistorico(String complementoHistorico) {
        this.complementoHistorico = complementoHistorico;
    }

    public String getContaDebitoNome() {
        return contaDebitoNome;
    }

    public void setContaDebitoNome(String contaDebitoNome) {
        this.contaDebitoNome = contaDebitoNome;
    }

    public String getContaCreditoNome() {
        return contaCreditoNome;
    }

    public void setContaCreditoNome(String contaCreditoNome) {
        this.contaCreditoNome = contaCreditoNome;
    }
}
