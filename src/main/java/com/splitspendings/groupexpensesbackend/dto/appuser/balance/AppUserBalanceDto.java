package com.splitspendings.groupexpensesbackend.dto.appuser.balance;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppUserBalanceDto {
    private BigDecimal balance;
    private Currency currency;
    private GroupDto group;
    private AppUserDto firstAppUser;
    private AppUserDto secondAppUser;
}
