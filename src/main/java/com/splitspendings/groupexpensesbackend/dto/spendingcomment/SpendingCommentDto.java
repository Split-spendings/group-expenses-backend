package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Spending;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SpendingCommentDto {

    private final Long id;
    private final String message;
    private final ZonedDateTime timeCreated;
    private final Spending spending;
    private final AppUser addedByAppUser;
}
