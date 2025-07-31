package br.com.codex.v1.domain.fiscal;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Model resposnsável por armazenar as informações da Impressão
 */
public class Impressao {

    @Setter
    @Getter
    private String xml;
    @Setter
    @Getter
    private InputStream jasper;
    @Setter
    @Getter
    private String pathExpression;
    private Map<String, Object> parametros;

    public Map<String, Object> getParametros() {
        if(this.parametros == null){
            this.parametros = new HashMap<>();
        }
        return parametros;
    }
}