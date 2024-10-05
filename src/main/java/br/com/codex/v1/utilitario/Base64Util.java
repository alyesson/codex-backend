package br.com.codex.v1.utilitario;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class Base64Util {
    public static String encode(String valor){
        String encoded = Base64.getEncoder().encodeToString(valor.getBytes());
        return encoded;
    }

    public String decode(String valor){
        byte[] decodeBytes = Base64.getDecoder().decode(valor);
        String decoded = new String(decodeBytes, StandardCharsets.UTF_8);
        return decoded;
    }
}
