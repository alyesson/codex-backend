package br.com.codex.v1.domain.enums;

public enum TipoOperacao {

    ENTRADA("0"), SAIDA("1");

    private final String descricao;

    TipoOperacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TipoOperacao getCodigo(final String codigo) {
        for (TipoOperacao cod : TipoOperacao.values()) {
            if (codigo.equals(cod.getDescricao())) {
                return cod;
            }
        }
        return null;
    }
}
