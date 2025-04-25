package br.com.codex.v1.domain.enums;

public enum TipoHorario {

    DIURNO(0, "DIURNO"), NOTURNO(1, "NOTURNO"), MISTO(2, "MISTO");

    private Integer codigo;
    private String descricao;

    private TipoHorario(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoHorario toEnum(Integer cod) {
        if(cod == null) {
            return null;
        }

        for(TipoHorario x : TipoHorario.values()) {
            if(cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Tpo de horário inválido");
    }
}
