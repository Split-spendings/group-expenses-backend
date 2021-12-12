package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserBalanceMapper {
    AppUserBalanceDto userBalanceToAppUserBalance(UserBalance userBalance);
    List<AppUserBalanceDto> userBalanceListToAppUserBalanceList(List<UserBalance> userBalanceList);
}
