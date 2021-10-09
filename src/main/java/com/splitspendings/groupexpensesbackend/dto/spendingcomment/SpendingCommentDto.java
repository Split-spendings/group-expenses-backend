package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class SpendingCommentDto {

    private final Long id;
    private final String message;
    private final ZonedDateTime timeAdded;
    private final SpendingCommentShortDto spending;
    private final AppUserDto addedByAppUser;
}