package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public class LancamentoContabilDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDate dataLancamento;
    private BigDecimal valor;
    private Long contaDebitoId;
    private String contaDebitoNome;
    private Long contaCreditoId;
    private String contaCreditoNome;
    private Long historicoPadraoId;
    private String historicoPadraoDescricao;
    private Long notaFiscalOrigemId;
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
}