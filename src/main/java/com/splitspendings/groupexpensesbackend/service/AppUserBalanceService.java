package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.Group;

public interface AppUserBalanceService {
    void recalculateAppUserBalanceByGroupId(Group group);
}
