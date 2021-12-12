package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GroupDto {

    private Long id;
    private String name;
    private ZonedDateTime timeCreated;
    private ZonedDateTime lastTimeOpened;
    private ZonedDateTime lastTimeClosed;
    private Boolean personal;
    private Boolean simplifyDebts;
    private Currency defaultCurrency;
    private Boolean isActiveMember;
}