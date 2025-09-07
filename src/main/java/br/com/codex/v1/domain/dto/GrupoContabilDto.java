package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class GrupoContabilDto {

    private String nome;
    private List<ItemContabilDto> itens;
    private BigDecimal total;

    // Construtores
    public GrupoContabilDto() {
    }

    public GrupoContabilDto(String nome, List<ItemContabilDto> itens) {
        this.nome = nome;
        this.itens = itens;
        this.total = calcularTotal();
    }

    public void setItens(List<ItemContabilDto> itens) {
        this.itens = itens;
        this.total = calcularTotal();
    }

    private BigDecimal calcularTotal() {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
                .map(ItemContabilDto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GrupoContabilDto that = (GrupoContabilDto) o;
        return Objects.equals(nome, that.nome) && Objects.equals(itens, that.itens) && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, itens, total);
    }

    @Override
    public String toString() {
        return "GrupoContabil{" +
                "nome='" + nome + '\'' +
                ", itens=" + itens +
                ", total=" + total +
                '}';
    }
}
