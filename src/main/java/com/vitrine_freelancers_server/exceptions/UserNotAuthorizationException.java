package com.vitrine_freelancers_server.exceptions;

public class UserNotAuthorizationException extends RuntimeException {
    public static final String USER_NOT_AUTHORIZATION_MESSAGE = "O usuário não está autorizado a acessar este recurso.";

    public UserNotAuthorizationException() {
        super(USER_NOT_AUTHORIZATION_MESSAGE);
    }


}
