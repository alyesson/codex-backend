package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.NotaFiscalDuplicatas;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class NotaFiscalDuplicatasDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long notaFiscalId;

    private String numeroDuplicata;
    private OffsetDateTime dataVencimento;
    private BigDecimal valorDuplicata;

    public NotaFiscalDuplicatasDto() {
        super();
    }

    public NotaFiscalDuplicatasDto(NotaFiscalDuplicatas obj) {
        this.id = obj.getId();
        this.notaFiscalId = obj.getNotaFiscal() != null ? obj.getNotaFiscal().getId() : null;
        this.numeroDuplicata = obj.getNumeroDuplicata();
        this.dataVencimento = obj.getDataVencimento();
        this.valorDuplicata = obj.getValorDuplicata();
    }
}

