package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.payoff.NewPayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.UpdatePayoffDto;
import com.splitspendings.groupexpensesbackend.service.PayoffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/payoffs")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class PayoffController {
    private final PayoffService payoffService;

    @GetMapping("/{id}")
    public PayoffDto getPayoffById(@PathVariable Long id) {
        return payoffService.payoffById(id);
    }

    @PostMapping
    public PayoffDto createPayoff(@RequestBody NewPayoffDto newPayoffDto) {
        return payoffService.createPayoff(newPayoffDto);
    }

    @PatchMapping("/{id}")
    public PayoffDto updatePayoff(@PathVariable Long id, @RequestBody UpdatePayoffDto updatePayoffDto) {
        return payoffService.updatePayoff(id, updatePayoffDto);
    }
}
