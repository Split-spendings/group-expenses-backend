package com.splitspendings.groupexpensesbackend.errorhandling;

import lombok.Data;

@Data
public class Violation {

    private final String fieldName;

    private final String message;
}