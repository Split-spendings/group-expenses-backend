package com.splitspendings.groupexpensesbackend.util;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LogUtil {

    private static final boolean SEND_MESSAGE_WITH_EXCEPTION = true;

    private LogUtil() {

    }

    public static ResponseStatusException logMessageAndReturnResponseStatusException(Logger log, HttpStatus status, String message) {
        log.info(message);
        if (SEND_MESSAGE_WITH_EXCEPTION) {
            return new ResponseStatusException(status, message);
        } else {
            return new ResponseStatusException(status);
        }
    }
}