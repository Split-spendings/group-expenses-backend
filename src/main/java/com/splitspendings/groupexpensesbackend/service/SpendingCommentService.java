package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;

public interface SpendingCommentService {
    SpendingCommentDto spendingCommentById(Long commentId);
    SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto);
    SpendingCommentDto updateSpendingCommentInfo(Long id, UpdateSpendingCommentDto updateSpendingCommentDto);
}
