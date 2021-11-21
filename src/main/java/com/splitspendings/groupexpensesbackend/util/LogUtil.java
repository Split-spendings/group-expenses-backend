package com.splitspendings.groupexpensesbackend.util;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LogUtil {

    private LogUtil() {

    }

    public static ResponseStatusException logMessageAndReturnResponseStatusException(Logger log, HttpStatus status, String message) {
        log.info(message);
        return new ResponseStatusException(status, message);
    }

    public static<T extends Exception> T logMessageAndReturnException(Logger log, T t){
        log.info(t.getMessage());
        return t;
    }
}