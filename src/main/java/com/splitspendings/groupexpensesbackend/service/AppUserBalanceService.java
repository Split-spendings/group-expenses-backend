package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.UserBalance;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;

import java.util.UUID;

public interface AppUserBalanceService {

    UserBalance appUserBalanceModelById(Long id);

    AppUserBalanceDto appUserBalanceById(Long id);

    Iterable<AppUserBalanceDto> appUserBalancesByCurrentAppUser();

    Iterable<AppUserBalanceDto> appUserBalancesByGroupId(Long groupId);

    Iterable<AppUserBalanceDto> appUserBalancesByGroupIdAndAppUserId(Long groupId, UUID appUserId);

    AppUserBalanceDto appUserBalanceByGroupIdAndAppUserIdAndCurrency(Long groupId, UUID appUserId, Currency currency);

    void recalculateAppUserBalanceByGroup(Group group);

    void recalculateAppUserBalanceByGroupAndCurrency(Group group, Currency currency);
}
