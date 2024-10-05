package br.com.codex.v1.utilitario;

import java.sql.Date;

public class ConverteStringEmDate {

    public Date converteStringEmDate(String data){
        return java.sql.Date.valueOf(data);
    }
}
