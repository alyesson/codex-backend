package br.com.codex.v1.utilitario;

public class RemoveCaracteresEspeciais {
    public static String removerCaracteresEspeciais(String origem) {
        return origem.replaceAll("[^0-9]", ""); // Remove tudo exceto números
    }
}
