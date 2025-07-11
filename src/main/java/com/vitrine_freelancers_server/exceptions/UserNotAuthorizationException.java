package com.vitrine_freelancers_server.exceptions;

public class UserNotAuthorizationException extends RuntimeException {
    public UserNotAuthorizationException(
            String message
    ) {
        super(message != null && !message.isEmpty() ? message : "Usuário não autorizado");
    }
}
