package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TabelaDeducaoInssDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private BigDecimal faixaSalario1;
    private BigDecimal aliquota1;
    private BigDecimal parcelaDeduzir1;
    private BigDecimal faixaSalario2;
    private BigDecimal aliquota2;
    private BigDecimal parcelaDeduzir2;
    private BigDecimal faixaSalario3;
    private BigDecimal aliquota3;
    private BigDecimal parcelaDeduzir3;
    private BigDecimal faixaSalario4;
    private BigDecimal aliquota4;
    private BigDecimal parcelaDeduzir4;
    private BigDecimal salarioFamilia;

    public TabelaDeducaoInssDto() {
        super();
    }

    public TabelaDeducaoInssDto(TabelaDeducaoInss obj) {
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
}
