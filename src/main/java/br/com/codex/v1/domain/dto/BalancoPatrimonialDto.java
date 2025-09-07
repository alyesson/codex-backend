package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class BalancoPatrimonialDto {

    private List<GrupoContabilDto> ativo;
    private List<GrupoContabilDto> passivo;
    private List<GrupoContabilDto> patrimonio;
    private BigDecimal totalAtivo;
    private BigDecimal totalPassivoPatrimonio;

    public BalancoPatrimonialDto() {
    }

    public BalancoPatrimonialDto(List<GrupoContabilDto> ativo, List<GrupoContabilDto> passivo, List<GrupoContabilDto> patrimonio) {
        this.ativo = ativo;
        this.passivo = passivo;
        this.patrimonio = patrimonio;
        this.totalAtivo = calcularTotalAtivo();
        this.totalPassivoPatrimonio = calcularTotalPassivoPatrimonio();
    }

    public void setAtivo(List<GrupoContabilDto> ativo) {
        this.ativo = ativo;
        this.totalAtivo = calcularTotalAtivo();
    }

    public void setPassivo(List<GrupoContabilDto> passivo) {
        this.passivo = passivo;
        this.totalPassivoPatrimonio = calcularTotalPassivoPatrimonio();
    }

    public void setPatrimonio(List<GrupoContabilDto> patrimonio) {
        this.patrimonio = patrimonio;
        this.totalPassivoPatrimonio = calcularTotalPassivoPatrimonio();
    }

    private BigDecimal calcularTotalAtivo() {
        if (ativo == null || ativo.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return ativo.stream()
                .map(GrupoContabilDto::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalPassivoPatrimonio() {
        BigDecimal totalPassivo = (passivo == null || passivo.isEmpty()) ?
                BigDecimal.ZERO :
                passivo.stream()
                        .map(GrupoContabilDto::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPatrimonio = (patrimonio == null || patrimonio.isEmpty()) ?
                BigDecimal.ZERO :
                patrimonio.stream()
                        .map(GrupoContabilDto::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPassivo.add(totalPatrimonio);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BalancoPatrimonialDto that = (BalancoPatrimonialDto) o;
        return Objects.equals(ativo, that.ativo) && Objects.equals(passivo, that.passivo)
                && Objects.equals(patrimonio, that.patrimonio)
                && Objects.equals(totalAtivo, that.totalAtivo)
                && Objects.equals(totalPassivoPatrimonio, that.totalPassivoPatrimonio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ativo, passivo, patrimonio, totalAtivo, totalPassivoPatrimonio);
    }

    // toString
    @Override
    public String toString() {
        return "BalancoPatrimonialResponse{" +
                "ativo=" + ativo +
                ", passivo=" + passivo +
                ", patrimonio=" + patrimonio +
                ", totalAtivo=" + totalAtivo +
                ", totalPassivoPatrimonio=" + totalPassivoPatrimonio +
                '}';
    }
}
