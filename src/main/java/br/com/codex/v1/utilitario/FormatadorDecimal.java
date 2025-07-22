package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatadorDecimal {

    private FormatadorDecimal() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Formata valores para 2 casas decimais com arredondamento HALF_UP
     */
    public static String formatar(BigDecimal valor) {
        if (valor == null || valor.equals(new BigDecimal("0.00"))) {
            return "0.00";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(valor);
    }

    /**
     * Para valores que podem vir como String
     */
    public static String formatar(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "0.00";
        }
        try {
            return formatar(new BigDecimal(valor.replace(",", ".")));
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }


    public static String formatarPeso(Double valor) {
        if (valor == null) {
            return "0.000";
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.000", symbols);
        return df.format(valor);
    }

}
