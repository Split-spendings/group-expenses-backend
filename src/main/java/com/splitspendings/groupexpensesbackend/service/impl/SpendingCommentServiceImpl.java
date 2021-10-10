package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingCommentMapper;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.repository.SpendingCommentRepository;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.SpendingCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SpendingCommentServiceImpl implements SpendingCommentService {

    private final SpendingCommentRepository spendingCommentRepository;

    private final IdentityService identityService;

    private final SpendingCommentMapper spendingCommentMapper;

    @Override
    public SpendingCommentDto spendingCommentById(Long commentId) {
        SpendingComment spendingComment = spendingCommentRepository
                .findById(commentId, identityService.currentUserID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Spending comment with id = {%d} not found", commentId)));
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    @Override
    public SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto) {
        return null;
    }
}