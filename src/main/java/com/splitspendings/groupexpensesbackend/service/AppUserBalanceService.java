package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.appuserbalance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.UserBalance;

public interface AppUserBalanceService {

    UserBalance appUserBalanceModelById(Long id);

    AppUserBalanceDto appUserBalanceById(Long id);

    void recalculateAppUserBalanceByGroup(Group group);
}
