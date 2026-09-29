package com.ice.harmonia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotAvailableException extends ResponseStatusException {

    public EntityNotAvailableException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
