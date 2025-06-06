package br.com.codex.v1.domain.enums;

public enum TipoAmbiente {
    PRODUCAO(1, "PRODUÇÃO"), HOMOLOGACAO(2, "HOMOLOGAÇÃO");

    private Integer codigo;
    private String descricao;

    private TipoAmbiente(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoAmbiente toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(TipoAmbiente x : TipoAmbiente.values()) {
            if(cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Ambiente de nota fiscal inválido");
    }
}
