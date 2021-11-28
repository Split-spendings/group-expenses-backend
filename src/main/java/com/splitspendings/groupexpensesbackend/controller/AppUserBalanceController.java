package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.appuser.balance.AppUserBalanceDto;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users/balance")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class AppUserBalanceController {
    private final AppUserBalanceService appUserBalanceService;

    @GetMapping("/{id}")
    public AppUserBalanceDto getAppUserBalanceById(@PathVariable Long id) {
        return appUserBalanceService.appUserBalanceById(id);
    }
}
