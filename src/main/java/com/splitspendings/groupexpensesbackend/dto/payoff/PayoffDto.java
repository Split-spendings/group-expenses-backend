package com.splitspendings.groupexpensesbackend.dto.payoff;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class PayoffDto {
    private String title;
    private BigDecimal amount;
    private ZonedDateTime timeCreated;
    private Currency currency;
    private GroupInfoDto group;
    private AppUserDto addedByAppUser;
    private AppUserDto paidForAppUser;
    private AppUserDto paidToAppUser;
}