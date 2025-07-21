package br.com.codex.v1.utilitario;

public class RemoveExcessoCfop {

    public static String limitarDescricao(String descricao){
        if (descricao == null) {
            return "";
        }
        return descricao.length() > 54 ? descricao.substring(0, 54) : descricao;
    }
}
