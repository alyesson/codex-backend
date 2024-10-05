package br.com.codex.v1.utilitario;

public class MinimizarPalavras {

    public static String minimizarPalavras(String frase) {
        if (frase == null || frase.isEmpty()) {
            return frase;
        }

        StringBuilder resultado = new StringBuilder();
        String[] palavras = frase.split("\\s+"); // Divide a frase em palavras

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                char primeiraLetra = Character.toLowerCase(palavra.charAt(0));
                String restoPalavra = palavra.substring(1);
                resultado.append(primeiraLetra).append(restoPalavra).append(" ");
            }
        }

        return resultado.toString().trim();
    }
}
