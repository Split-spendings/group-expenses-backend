package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.share.ShareDto;
import com.splitspendings.groupexpensesbackend.dto.spending.NewSpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingCommentsDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.model.Item;
import com.splitspendings.groupexpensesbackend.model.Share;
import com.splitspendings.groupexpensesbackend.model.Spending;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GroupMembershipMapper.class, SpendingCommentMapper.class, ShareMapper.class})
public interface SpendingMapper {

    SpendingShortDto spendingToSpendingShortDto(Spending spending);

    List<SpendingShortDto> spendingListToSpendingShortDtoList(List<Spending> spendingList);

    @Mapping(target = "shares", source = "items")
    SpendingDto spendingToSpendingDto(Spending spending);

    Spending newSpendingDtoToSpending(NewSpendingDto newSpendingDto);

    SpendingCommentsDto spendingToSpendingCommentsDto(Spending spending);

    Set<ShareDto> shareSetToShareDtoSet(Set<Share> shareSet);

    default Set<ShareDto> itemSetToShareSetDto(Set<Item> items){
        if (items == null){
            return null;
        }

        Set<Share> shareSet = new HashSet<>();
        items.stream().map(Item::getShares).forEach(shareSet::addAll);

        return shareSetToShareDtoSet(shareSet);
    }
}
