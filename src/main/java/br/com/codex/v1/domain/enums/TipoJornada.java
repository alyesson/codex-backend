package br.com.codex.v1.domain.enums;

public enum TipoJornada {

    ESCALA_4_2(0, "4x2"), ESCALA_5_1(1, "5x1"), ESCALA_5_2(2, "5x2"), ESCALA_6_1(3, "6x1"), ESCALA_12_36(4, "12x36"), ESCALA_24_48(5, "24x48");

    private Integer codigo;
    private String descricao;

    private TipoJornada(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoJornada toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(TipoJornada x : TipoJornada.values()) {
            if(cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Tipo de jornada inválida");
    }
}
