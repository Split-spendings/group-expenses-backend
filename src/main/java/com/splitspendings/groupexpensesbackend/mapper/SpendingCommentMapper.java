package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpendingCommentMapper {
    SpendingCommentDto spendingCommentToSpendingCommentDto(SpendingComment spendingComment);
}
