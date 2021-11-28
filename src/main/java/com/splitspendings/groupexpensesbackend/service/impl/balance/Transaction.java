package com.splitspendings.groupexpensesbackend.service.impl.balance;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transaction {
    private AppUser from;
    private AppUser to;
    private BigDecimal amount;
    private Currency currency;
}