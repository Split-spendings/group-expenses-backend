package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.Group;

public interface AppUserBalanceService {
    void recalculateAppUserBalanceByGroup(Group group);
}
