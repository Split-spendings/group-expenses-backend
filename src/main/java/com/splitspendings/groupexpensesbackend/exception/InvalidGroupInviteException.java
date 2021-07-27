package com.splitspendings.groupexpensesbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidGroupInviteException extends ResponseStatusException {

    public InvalidGroupInviteException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
