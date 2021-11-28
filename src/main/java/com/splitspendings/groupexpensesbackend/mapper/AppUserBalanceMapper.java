package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserBalanceMapper {
    AppUserBalanceDto userBalanceToAppUserBalance(UserBalance userBalance);
}
