package com.vitrine_freelancers_server.exceptions;

public class UserNotFoundException extends RuntimeException {
    public static final String USER_NOT_FOUND_MESSAGE = "User not found.";

    public UserNotFoundException() {
        super(USER_NOT_FOUND_MESSAGE);
    }


}
