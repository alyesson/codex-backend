package br.com.codex.v1.utilitario;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<ValidCep, String> {
    @Override
    public void initialize(ValidCep constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null) {
            return true; // permite valor nulo, caso seja opcional
        }

        // Remove caracteres não numéricos antes de validar o formato
        cep = cep.replaceAll("[^\\d]", "");

        // Verifica se o CEP tem 8 dígitos
        if (!cep.matches("\\d{8}")) {
            return false;
        }

        // Consulta à API ViaCEP
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                return !jsonObject.has("erro"); // se "erro" estiver presente, o CEP não existe
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // considera inválido em caso de erro na consulta
        }
    }
}

