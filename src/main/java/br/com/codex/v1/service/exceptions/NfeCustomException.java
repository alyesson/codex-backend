package br.com.codex.v1.service.exceptions;

public class NfeCustomException extends RuntimeException {
    public NfeCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
