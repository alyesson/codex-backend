package br.com.codex.v1.utilitario;

import org.springframework.stereotype.Component;

/**
 * Classe utilitária para tratamento de strings, removendo espaços em branco
 * e substituindo por underline, além de outras formatações úteis
 */
@Component
public class RemoveEspacoBranco {

    /**
     * Remove todos os espaços em branco de uma string e substitui por underline
     * Exemplo: "nome codigo" → "nome_codigo"
     *
     * @param texto O texto a ser formatado
     * @return String formatada sem espaços, com underlines
     */
    public static String removerEspacos(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        // Remove espaços extras e substitui espaços por underline
        return texto.trim()
                .replaceAll("\\s+", "_")  // Substitui um ou mais espaços por underline
                .replaceAll("_+", "_");   // Garante que não haja underlines consecutivos
    }

    /**
     * Remove espaços em branco sem substituir por underline
     * Exemplo: "nome codigo" → "nomecodigo"
     *
     * @param texto O texto a ser formatado
     * @return String sem espaços
     */
    public static String removerEspacosCompletamente(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        return texto.replaceAll("\\s+", "");
    }

    /**
     * Formata para snake_case (tudo minúsculo com underlines)
     * Exemplo: "Nome Codigo" → "nome_codigo"
     *
     * @param texto O texto a ser formatado
     * @return String em snake_case
     */
    public static String paraSnakeCase(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        return removerEspacos(texto).toLowerCase();
    }

    /**
     * Formata para camelCase
     * Exemplo: "nome codigo" → "nomeCodigo"
     *
     * @param texto O texto a ser formatado
     * @return String em camelCase
     */
    public static String paraCamelCase(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        String[] palavras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder(palavras[0].toLowerCase());

        for (int i = 1; i < palavras.length; i++) {
            if (!palavras[i].isEmpty()) {
                String palavra = palavras[i].substring(0, 1).toUpperCase() +
                        palavras[i].substring(1).toLowerCase();
                resultado.append(palavra);
            }
        }

        return resultado.toString();
    }

    /**
     * Remove caracteres especiais e mantém apenas letras, números e underlines
     * Exemplo: "nome@código#123" → "nome_codigo_123"
     *
     * @param texto O texto a ser sanitizado
     * @return String sanitizada
     */
    public static String sanitizarString(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        return texto.trim()
                .replaceAll("[^a-zA-Z0-9\\s]", "")  // Remove caracteres especiais
                .replaceAll("\\s+", "_")            // Substitui espaços por underline
                .toLowerCase();                     // Converte para minúsculo
    }

    /**
     * Verifica se uma string contém espaços
     *
     * @param texto O texto a ser verificado
     * @return true se contém espaços, false caso contrário
     */
    public static boolean contemEspacos(String texto) {
        return texto != null && texto.contains(" ");
    }
}