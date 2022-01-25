package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.payoff.NewPayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.UpdatePayoffDto;
import com.splitspendings.groupexpensesbackend.model.Payoff;

public interface PayoffService {

    Payoff payoffModelById(Long id);

    PayoffDto payoffById(Long id);

    PayoffDto createPayoff(NewPayoffDto newPayoffDto);

    PayoffDto updatePayoff(Long id, UpdatePayoffDto updatePayoffDto);

    void deletePayoff(Long id);
}