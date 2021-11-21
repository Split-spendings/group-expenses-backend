package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.spending.comment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spending.comment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpendingCommentMapper {
    SpendingCommentDto spendingCommentToSpendingCommentDto(SpendingComment spendingComment);

    SpendingComment copyUpdateSpendingCommentDtoToSpendingComment(UpdateSpendingCommentDto updateSpendingCommentDto, @MappingTarget SpendingComment spendingComment);

}
