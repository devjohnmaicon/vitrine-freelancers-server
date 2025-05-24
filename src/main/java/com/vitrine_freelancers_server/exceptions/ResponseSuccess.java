package com.vitrine_freelancers_server.exceptions;

public record ResponseSuccess(
        String message,
        Integer status_code,
        Object data
) {


}
