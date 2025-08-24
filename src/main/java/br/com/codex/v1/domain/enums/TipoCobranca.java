package br.com.codex.v1.domain.enums;

public enum TipoCobranca {

    HORA(1, "Por Hora"), DIA(2, "Por Dia"), UNIDADE(3, "Por Unidade"), METRO_QUADRADO(4, "Por m²"), VISITA(5, "Por Visita");

    private Integer codigo;
    private String descricao;

    private TipoCobranca(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCobranca toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(TipoCobranca x : TipoCobranca.values()) {
            if(cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Tipo de cobrança inválido");
    }
}
