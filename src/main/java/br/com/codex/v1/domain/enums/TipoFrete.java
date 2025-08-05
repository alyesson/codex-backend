package br.com.codex.v1.domain.enums;

public enum TipoFrete {

    EMITENTE("0"), DESTINATARIO("1"), TERCEIROS("2"), SEM_FRETE("9");

    private final String descricao;

    TipoFrete(String descricao) {
        this.descricao = descricao;
    }

    public static TipoFrete getTipoFrete(String mod) {
        switch (mod) {
            case "1":
                return TipoFrete.DESTINATARIO;
            case "2":
                return TipoFrete.TERCEIROS;
            case "9":
                return TipoFrete.SEM_FRETE;
            default:
                return TipoFrete.EMITENTE;
        }
    }

    public String getDescricao() {
        return this.descricao;
    }
}
