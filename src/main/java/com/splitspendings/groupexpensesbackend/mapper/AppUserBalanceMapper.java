package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.balance.BalanceDto;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {GroupMapper.class, AppUserMapper.class})
public interface AppUserBalanceMapper {

    AppUserBalanceDto userBalanceToAppUserBalance(UserBalance userBalance);

    List<AppUserBalanceDto> userBalanceListToAppUserBalanceList(List<UserBalance> userBalanceList);

    BalanceDto userBalanceToBalanceDto(UserBalance userBalance);
}
