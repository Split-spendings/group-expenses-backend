package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.model.Spending;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpendingMapper {

    SpendingShortDto groupSpendingToSpendingShortDto(Spending spending);

    List<SpendingShortDto> groupSpendingListToSpendingShortDtoList(List<Spending> spendingList);
}
