package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class DREDto {

    private List<GrupoContabilDto> receitas;
    private List<GrupoContabilDto> custos;
    private List<GrupoContabilDto> despesas;
    private BigDecimal totalReceitas;
    private BigDecimal totalCustos;
    private BigDecimal totalDespesas;
    private BigDecimal resultadoOperacional;
    private BigDecimal resultadoLiquido;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String empresaNome;

    // Construtores
    public DREDto() {
    }

    public DREDto(List<GrupoContabilDto> receitas, List<GrupoContabilDto> custos, List<GrupoContabilDto> despesas,
                  LocalDate dataInicial, LocalDate dataFinal, String empresaNome) {
        this.receitas = receitas;
        this.custos = custos;
        this.despesas = despesas;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.empresaNome = empresaNome;
        this.totalReceitas = calcularTotalReceitas();
        this.totalCustos = calcularTotalCustos();
        this.totalDespesas = calcularTotalDespesas();
        this.resultadoOperacional = calcularResultadoOperacional();
        this.resultadoLiquido = calcularResultadoLiquido();
    }

    public void setReceitas(List<GrupoContabilDto> receitas) {
        this.receitas = receitas;
        this.totalReceitas = calcularTotalReceitas();
        this.resultadoOperacional = calcularResultadoOperacional();
        this.resultadoLiquido = calcularResultadoLiquido();
    }

    public void setCustos(List<GrupoContabilDto> custos) {
        this.custos = custos;
        this.totalCustos = calcularTotalCustos();
        this.resultadoOperacional = calcularResultadoOperacional();
        this.resultadoLiquido = calcularResultadoLiquido();
    }

    public void setDespesas(List<GrupoContabilDto> despesas) {
        this.despesas = despesas;
        this.totalDespesas = calcularTotalDespesas();
        this.resultadoOperacional = calcularResultadoOperacional();
        this.resultadoLiquido = calcularResultadoLiquido();
    }

    private BigDecimal calcularTotalReceitas() {
        if (receitas == null || receitas.isEmpty()) return BigDecimal.ZERO;
        return receitas.stream()
                .map(GrupoContabilDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalCustos() {
        if (custos == null || custos.isEmpty()) return BigDecimal.ZERO;
        return custos.stream()
                .map(GrupoContabilDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalDespesas() {
        if (despesas == null || despesas.isEmpty()) return BigDecimal.ZERO;
        return despesas.stream()
                .map(GrupoContabilDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularResultadoOperacional() {
        return totalReceitas.subtract(totalCustos).subtract(totalDespesas);
    }

    private BigDecimal calcularResultadoLiquido() {
        return resultadoOperacional; // Pode incluir outros resultados não operacionais se necessário
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DREDto dre = (DREDto) o;
        return Objects.equals(receitas, dre.receitas) &&
                Objects.equals(custos, dre.custos) &&
                Objects.equals(despesas, dre.despesas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receitas, custos, despesas);
    }

    @Override
    public String toString() {
        return "DRE{" +
                "receitas=" + receitas +
                ", custos=" + custos +
                ", despesas=" + despesas +
                ", totalReceitas=" + totalReceitas +
                ", totalCustos=" + totalCustos +
                ", totalDespesas=" + totalDespesas +
                ", resultadoOperacional=" + resultadoOperacional +
                ", resultadoLiquido=" + resultadoLiquido +
                '}';
    }
}
