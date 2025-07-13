package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatadorDecimal {

    private FormatadorDecimal() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Formata valores para 2 casas decimais com arredondamento HALF_UP
     */
    public static String formatar(BigDecimal valor) {
        if (valor == null) {
            return "0.00";
        }
        return valor.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String formatar(Double valor) {
        if (valor == null) {
            return "0.00";
        }
        return new BigDecimal(valor.toString()).setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * Para valores que podem vir como String
     */
    public static String formatar(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "0.00";
        }
        try {
            return new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP).toString();
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
}
