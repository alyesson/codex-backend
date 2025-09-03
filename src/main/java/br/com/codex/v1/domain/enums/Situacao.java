package br.com.codex.v1.domain.enums;

public enum Situacao {

    ABERTO(0, "ABERTO"), CANCELADO(1, "CANCELADO"), ATUANDO(2, "ATUANDO"), FECHADO(3, "FECHADO"),
    PENDENTE(4, "PENDENTE"), APROVADO(5, "APROVADO"), REJEITADO(6, "REJEITADO"), DEVOLUCAO(7, "DEVOLUCAO"),
    FINALIZADO(8, "FINALIZADO");

    private Integer codigo;
    private String descricao;

    private Situacao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Situacao toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(Situacao x : Situacao.values()) {
            if(cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Situação inválida");
    }
}