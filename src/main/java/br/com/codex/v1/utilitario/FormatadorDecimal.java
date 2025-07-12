package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatadorDecimal {

    private FormatadorDecimal() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Formata um BigDecimal com 2 casas decimais usando arredondamento HALF_UP
     * @param valor Valor a ser formatado (pode ser nulo)
     * @return String formatada ou "0.00" se valor for nulo
     */
    public static String formatarComDuasCasas(BigDecimal valor) {
        return valor != null ?
                valor.setScale(2, RoundingMode.HALF_UP).toString() :
                "0.00";
    }

    /**
     * Versão para valores double
     */
    public static String formatarComDuasCasas(Double valor) {
        return valor != null ?
                new BigDecimal(valor.toString()).setScale(2, RoundingMode.HALF_UP).toString() :
                "0.00";
    }
}
