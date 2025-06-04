package br.com.codex.v1.domain.contabilidade;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class NotaFiscalDuplicatas {
    @Id
    @GeneratedValue
    private Long id;

    private String numeroDuplicata;
    private OffsetDateTime dataVencimento;
    private BigDecimal valorDuplicata;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id")
    private NotaFiscal notaFiscal;

    public NotaFiscalDuplicatas() {
        super();
    }

    public NotaFiscalDuplicatas(Long id, String numeroDuplicata, OffsetDateTime dataVencimento, BigDecimal valorDuplicata, NotaFiscal notaFiscal) {
        this.id = id;
        this.numeroDuplicata = numeroDuplicata;
        this.dataVencimento = dataVencimento;
        this.valorDuplicata = valorDuplicata;
        this.notaFiscal = notaFiscal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscalDuplicatas that = (NotaFiscalDuplicatas) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
