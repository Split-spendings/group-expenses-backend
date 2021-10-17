package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/spendings")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class SpendingController {

    private final SpendingService spendingService;

    @GetMapping("/{id}")
    public SpendingDto spendingById(@PathVariable Long id) {
        return spendingService.spendingById(id);
    }

    @PostMapping
    public SpendingDto createSpending(@RequestBody NewSpendingDto newSpendingDto) {
        return spendingService.createSpending(newSpendingDto);
    }

    @GetMapping("/{id}/comments")
    public SpendingCommentsDto getSpendingComments(@PathVariable Long id) {
        return spendingService.getSpendingComments(id);
    }
}
