package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/fake")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class FakeController {
    private final AppUserBalanceService appUserBalanceService;

    @GetMapping("/{id}")
    public void groupInfo(@PathVariable Long id) {
        appUserBalanceService.recalculateAppUserBalanceByGroupId(id);
    }
}
