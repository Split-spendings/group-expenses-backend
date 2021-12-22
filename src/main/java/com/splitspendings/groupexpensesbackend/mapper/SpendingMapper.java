package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.model.Spending;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GroupMembershipMapper.class, SpendingCommentMapper.class})
public interface SpendingMapper {

    SpendingShortDto spendingToSpendingShortDto(Spending spending);

    List<SpendingShortDto> spendingListToSpendingShortDtoList(List<Spending> spendingList);

    SpendingDto spendingToSpendingDto(Spending spending);

    Spending newSpendingDtoToSpending(NewSpendingDto newSpendingDto);

    SpendingCommentsDto spendingToSpendingCommentsDto(Spending spending);
}
