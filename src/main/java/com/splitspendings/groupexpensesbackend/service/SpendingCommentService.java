package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.spending.comment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spending.comment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spending.comment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;

public interface SpendingCommentService {

    SpendingComment spendingCommentModelById(Long id);

    SpendingCommentDto spendingCommentById(Long commentId);

    SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto);

    SpendingCommentDto updateSpendingComment(Long id, UpdateSpendingCommentDto updateSpendingCommentDto);
}
