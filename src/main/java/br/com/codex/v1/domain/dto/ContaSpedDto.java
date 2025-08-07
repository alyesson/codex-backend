package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.Contas;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaSpedDto {
    private String codigo;      // Código da conta (ex: "1.1.02.101.2")
    private String nome;        // Nome da conta (ex: "CAIXA E BANCOS")
    private String natureza;    // Código da natureza (ex: "01" para Ativo)
    private String tipo;        // "S" (Sintética) ou "A" (Analítica)
    private int nivel;          // Nível hierárquico (ex: 0 para raiz, 1 para subconta)

    // Construtor padrão (obrigatório para frameworks como Jackson)
    public ContaSpedDto() {
    }

    // Construtor com campos essenciais
    public ContaSpedDto(String codigo, String nome, String natureza, String tipo, int nivel) {
        this.codigo = codigo;
        this.nome = nome;
        this.natureza = natureza;
        this.tipo = tipo;
        this.nivel = nivel;
    }

    // Métudo estático para criar DTO a partir da entidade Contas
    public static ContaSpedDto fromContas(Contas conta) {
        if (conta == null) {
            return null;
        }
        return new ContaSpedDto(
                conta.getConta(),
                conta.getNome(),
                conta.getNatureza(),
                conta.getTipo(),
                conta.getConta().split("\\.").length - 1 // Calcula o nível
        );
    }
}