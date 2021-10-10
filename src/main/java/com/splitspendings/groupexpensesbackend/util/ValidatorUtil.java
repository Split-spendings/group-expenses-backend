package com.splitspendings.groupexpensesbackend.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {

    private ValidatorUtil() {
    }

    public static<T> void validate(Validator validator, T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}