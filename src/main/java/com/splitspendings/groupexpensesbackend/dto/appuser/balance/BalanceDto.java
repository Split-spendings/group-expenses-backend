package com.splitspendings.groupexpensesbackend.dto.appuser.balance;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BalanceDto {
    private Long id;
    private BigDecimal balance;
    private Currency currency;
    private AppUserDto withAppUser;

    public void negate(){
        balance = balance.negate();
    }
}
