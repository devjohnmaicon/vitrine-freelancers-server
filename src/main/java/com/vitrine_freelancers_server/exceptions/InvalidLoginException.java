package com.vitrine_freelancers_server.exceptions;

public class InvalidLoginException extends RuntimeException {
    public static final String INVALID_LOGIN_MESSAGE = "Credenciais inválidas. Por favor, verifique seu e-mail e senha.";

    public InvalidLoginException() {
        super(INVALID_LOGIN_MESSAGE);
    }


}
