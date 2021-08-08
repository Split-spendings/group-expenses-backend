package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.item.NewItemDto;
import com.splitspendings.groupexpensesbackend.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item newItemDtoToItem(NewItemDto newItemDto);
}
