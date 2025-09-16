package br.com.codex.v1.utilitario;

public class MaximizarPalavras {

    public static String maximizarPalavras(String frase) {
        if (frase == null || frase.isEmpty()) {
            return frase;
        }

        StringBuilder resultado = new StringBuilder();
        String[] palavras = frase.split("\\s+"); // Divide a frase em palavras

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                // Converte todas as letras para maiúsculo, mantendo números no lugar
                String palavraMaximizada = maximizarLetras(palavra);
                resultado.append(palavraMaximizada).append(" ");
            }
        }

        return resultado.toString().trim();
    }

    private static String maximizarLetras(String palavra) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            if (Character.isLetter(c)) {
                resultado.append(Character.toUpperCase(c));
            } else {
                resultado.append(c); // Mantém números e outros caracteres
            }
        }
        return resultado.toString();
    }
}
