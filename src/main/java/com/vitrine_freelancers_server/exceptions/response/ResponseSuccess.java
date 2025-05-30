package com.vitrine_freelancers_server.exceptions.response;

public record ResponseSuccess(
        String message,
        Integer status_code,
        Object data
) {
    ResponseSuccess(String message, Integer status_code) {
        this(message, status_code, null);
    }

}
