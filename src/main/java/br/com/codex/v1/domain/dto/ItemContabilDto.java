package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class ItemContabilDto {

    private String nome;
    private BigDecimal valor;

    // Construtores
    public ItemContabilDto() {
    }

    public ItemContabilDto(String nome, BigDecimal valor) {
        this.nome = nome;
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemContabilDto that = (ItemContabilDto) o;
        return Objects.equals(nome, that.nome) && Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, valor);
    }

    // toString
    @Override
    public String toString() {
        return "ItemContabil{" +
                "nome='" + nome + '\'' +
                ", valor=" + valor +
                '}';
    }
}
