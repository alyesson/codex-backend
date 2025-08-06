package br.com.codex.v1.domain.enums;

import lombok.Getter;

@Getter
public enum UnidadeMedida {

    UNIDADE("UN", "Unidade"),
    KILOGRAMA("KG", "quilograma"),
    GRAMA("G", "Grama"),
    LITRO("LT", "Litro"),
    METRO("M", "Metro"),
    METRO_QUADRADO("M2", "Metro quadrado"),
    METRO_CUBICO("M3", "Metro cúbico"),
    PARES("PR", "Pares"),
    DUZIA("DZ", "Dúzia"),
    HORA("HR", "Hora"),
    MINUTO("MIN", "Minuto"),
    CAIXA("CX", "Caixa"),
    SACO("SC", "Saco"),
    TONELADA("TON", "Tonelada"),
    QUILOWATT_HORA("KWH", "Quilowatt-hora"),
    FOLHA("FL", "Folha"),
    GARRAFA("GF", "Garrafa"),
    ROLO("RL", "Rolo"),
    KIT("KT", "Kit"),
    LATA("LA", "Lata"),
    AMPOLA("AM", "Ampola"),
    BOBINA("BO", "Bobina"),
    BALDE("BD", "Balde"),
    BARRIL("BR", "Barril"),
    CONJUNTO("CJ", "Conjunto"),
    CENTO("CT", "Cento"),
    MILHEIRO("MIL", "Milheiro"),
    JOGO("JG", "Jogo"),
    PACOTE("PC", "Pacote"),
    RESMA("RM", "Resma"),
    SERVICE("SV", "Service (Unidade de serviço)"),
    TUBO("TB", "Tubo");

    private final String codigo;
    private final String descricao;

    UnidadeMedida(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    // Métudo para buscar por código
    public static UnidadeMedida fromCodigo(String codigo) {
        for (UnidadeMedida unidade : values()) {
            if (unidade.getCodigo().equalsIgnoreCase(codigo)) {
                return unidade;
            }
        }
        throw new IllegalArgumentException("Código de unidade inválido: " + codigo);
    }
}
