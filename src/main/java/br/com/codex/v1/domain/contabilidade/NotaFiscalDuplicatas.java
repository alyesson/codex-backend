package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class NotaFiscalDuplicatas implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroDuplicata;
    private LocalDate dataVencimento;
    private BigDecimal valorDuplicata;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id")
    private NotaFiscal notaFiscal;

    public NotaFiscalDuplicatas() {
        super();
    }

    public NotaFiscalDuplicatas(Long id, String numeroDuplicata, LocalDate dataVencimento, BigDecimal valorDuplicata, NotaFiscal notaFiscal) {
        this.id = id;
        this.numeroDuplicata = numeroDuplicata;
        this.dataVencimento = dataVencimento;
        this.valorDuplicata = valorDuplicata;
        this.notaFiscal = notaFiscal;
    }

    public NotaFiscalDuplicatas(NotaFiscalDuplicatasDto obj) {
        this.id = obj.getId();
        this.numeroDuplicata = obj.getNumeroDuplicata();
        this.dataVencimento = obj.getDataVencimento();
        this.valorDuplicata = obj.getValorDuplicata();
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