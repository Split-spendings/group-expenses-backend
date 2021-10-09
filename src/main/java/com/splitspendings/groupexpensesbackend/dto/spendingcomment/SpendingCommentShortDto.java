package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SpendingCommentShortDto {

    private final Long id;
    private final String message;
    private final ZonedDateTime timeAdded;
}