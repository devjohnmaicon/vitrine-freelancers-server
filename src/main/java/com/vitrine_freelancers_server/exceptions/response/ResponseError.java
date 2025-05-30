package com.vitrine_freelancers_server.exceptions.response;

public record ResponseError(String message, String error, int status, String path) {
    public ResponseError(String message, String error, int status) {
        this(message, error, status, null);
    }
}
