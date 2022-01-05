package com.splitspendings.groupexpensesbackend.dto.appuser.balance;

import java.util.List;
import lombok.Data;

@Data
public class GroupBalancesDto {
    private Long groupId;
    private List<BalanceDto> balances;
}
