package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.balance.GroupBalancesDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/balances")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class BalanceController {

    private final AppUserBalanceService appUserBalanceService;

    @GetMapping
    public Iterable<AppUserBalanceDto> getAppUserBalanceBy() {
        return appUserBalanceService.appUserBalancesByCurrentAppUser();
    }

    @GetMapping("/{groupId}")
    public GroupBalancesDto getAppUserBalancesByGroupId(@PathVariable Long groupId) {
        return appUserBalanceService.appUserBalancesByGroupId(groupId);
    }

    @GetMapping("/{groupId}/{appUserId}")
    public Iterable<AppUserBalanceDto> getAppUserBalance(@PathVariable Long groupId, @PathVariable UUID appUserId) {
       return appUserBalanceService.appUserBalancesByGroupIdAndAppUserId(groupId, appUserId);
    }

    @GetMapping("/{groupId}/{appUserId}/{currency}")
    public AppUserBalanceDto getAppUserBalance(@PathVariable Long groupId, @PathVariable UUID appUserId, @PathVariable Currency currency) {
        return appUserBalanceService.appUserBalanceByGroupIdAndAppUserIdAndCurrency(groupId, appUserId, currency);
    }
}
