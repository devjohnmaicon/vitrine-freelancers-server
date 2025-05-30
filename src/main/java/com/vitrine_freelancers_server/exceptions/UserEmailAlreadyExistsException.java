package com.vitrine_freelancers_server.exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public static final String USER_EMAIL_ALL_READY_EXISTS_MESSAGE = "The email already exists.";

    public UserEmailAlreadyExistsException() {
        super(USER_EMAIL_ALL_READY_EXISTS_MESSAGE);
    }


}
