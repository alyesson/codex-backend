package br.com.codexloja.v1.utilitario;

public class CapitalizarPalavras {

    public static String capitalizarPalavras(String frase) {
        if (frase == null || frase.isEmpty()) {
            return frase;
        }

        StringBuilder resultado = new StringBuilder();
        String[] palavras = frase.split("\\s+"); // Divide a frase em palavras

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                char primeiraLetra = Character.toUpperCase(palavra.charAt(0));
                String restoPalavra = palavra.substring(1).toLowerCase();
                resultado.append(primeiraLetra).append(restoPalavra).append(" ");
            }
        }

        return resultado.toString().trim();
    }
}
