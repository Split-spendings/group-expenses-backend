package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.spending.comment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spending.comment.SpendingCommentShortDto;
import com.splitspendings.groupexpensesbackend.dto.spending.comment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, SpendingMapper.class})
public interface SpendingCommentMapper {

    SpendingCommentDto spendingCommentToSpendingCommentDto(SpendingComment spendingComment);

    SpendingCommentShortDto spendingCommentToSpendingCommentShortDto(SpendingComment spendingComment);

    List<SpendingCommentShortDto> spendingCommentSetToSpendingCommentShortDtoList(Set<SpendingComment> set);

    void copyUpdateSpendingCommentDtoToSpendingComment(UpdateSpendingCommentDto updateSpendingCommentDto, @MappingTarget SpendingComment spendingComment);

}
