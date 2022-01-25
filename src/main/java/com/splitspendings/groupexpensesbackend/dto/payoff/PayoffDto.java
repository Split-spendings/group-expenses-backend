package com.splitspendings.groupexpensesbackend.dto.payoff;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class PayoffDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private ZonedDateTime timeCreated;
    private Currency currency;
    private AppUserDto addedByAppUser;
    private AppUserDto paidForAppUser;
    private AppUserDto paidToAppUser;
}