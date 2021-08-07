package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import lombok.Data;

import java.util.List;

@Data
public class GroupSpendingsDto {

    private Long id;
    private String name;
    private List<SpendingShortDto> spendings;
}
