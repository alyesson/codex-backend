package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.TabelaDeducaoInssDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class TabelaDeducaoInss implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal faixaSalario1;
    private BigDecimal aliquota1;
    private BigDecimal parcelaDeduzir1;

    @Column(precision = 10, scale = 2)
    private BigDecimal faixaSalario2;
    private BigDecimal aliquota2;

    @Column(precision = 10, scale = 2)
    private BigDecimal parcelaDeduzir2;

    @Column(precision = 10, scale = 2)
    private BigDecimal faixaSalario3;
    private BigDecimal aliquota3;

    @Column(precision = 10, scale = 2)
    private BigDecimal parcelaDeduzir3;

    @Column(precision = 10, scale = 2)
    private BigDecimal faixaSalario4;
    private BigDecimal aliquota4;

    @Column(precision = 10, scale = 2)
    private BigDecimal parcelaDeduzir4;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioFamilia;

    public TabelaDeducaoInss() {
        super();
    }

    public TabelaDeducaoInss(Long id, BigDecimal faixaSalario1, BigDecimal aliquota1, BigDecimal parcelaDeduzir1,
                             BigDecimal faixaSalario2, BigDecimal aliquota2, BigDecimal parcelaDeduzir2, BigDecimal faixaSalario3,
                             BigDecimal aliquota3, BigDecimal parcelaDeduzir3, BigDecimal faixaSalario4, BigDecimal aliquota4,
                             BigDecimal parcelaDeduzir4, BigDecimal salarioFamilia) {
        this.id = id;
        this.faixaSalario1 = faixaSalario1;
        this.aliquota1 = aliquota1;
        this.parcelaDeduzir1 = parcelaDeduzir1;
        this.faixaSalario2 = faixaSalario2;
        this.aliquota2 = aliquota2;
        this.parcelaDeduzir2 = parcelaDeduzir2;
        this.faixaSalario3 = faixaSalario3;
        this.aliquota3 = aliquota3;
        this.parcelaDeduzir3 = parcelaDeduzir3;
        this.faixaSalario4 = faixaSalario4;
        this.aliquota4 = aliquota4;
        this.parcelaDeduzir4 = parcelaDeduzir4;
        this.salarioFamilia = salarioFamilia;
    }

    public TabelaDeducaoInss(TabelaDeducaoInssDto obj) {
        this.id = obj.getId();
        this.faixaSalario1 = obj.getFaixaSalario1();
        this.aliquota1 = obj.getAliquota1();
        this.parcelaDeduzir1 = obj.getParcelaDeduzir1();
        this.faixaSalario2 = obj.getFaixaSalario2();
        this.aliquota2 = obj.getAliquota2();
        this.parcelaDeduzir2 = obj.getParcelaDeduzir2();
        this.faixaSalario3 = obj.getFaixaSalario3();
        this.aliquota3 = obj.getAliquota3();
        this.parcelaDeduzir3 = obj.getParcelaDeduzir3();
        this.faixaSalario4 = obj.getFaixaSalario4();
        this.aliquota4 = obj.getAliquota4();
        this.parcelaDeduzir4 = obj.getParcelaDeduzir4();
        this.salarioFamilia = obj.getSalarioFamilia();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TabelaDeducaoInss that = (TabelaDeducaoInss) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
