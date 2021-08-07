package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpendingShortDto {

    private Long id;
    private String title;
    private BigDecimal totalAmount;
    private Currency currency;
}
