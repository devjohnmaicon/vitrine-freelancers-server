package com.vitrine_freelancers_server.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException {
    public static final String COMPANY_ALL_READY_EXISTS_MESSAGE = "There is already a company with this name.";

    public CompanyAlreadyExistsException() {
        super(COMPANY_ALL_READY_EXISTS_MESSAGE);
    }


}
