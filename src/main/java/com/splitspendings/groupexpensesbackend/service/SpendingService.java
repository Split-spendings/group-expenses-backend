package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.model.Spending;

public interface SpendingService {

    Spending spendingModelById(Long id);

    SpendingDto spendingById(Long id);

    SpendingShortDto createSpending(NewSpendingDto newSpendingDto);

    SpendingCommentsDto getSpendingComments(Long spendingId);

    void deleteSpendingById(Long id);
}
