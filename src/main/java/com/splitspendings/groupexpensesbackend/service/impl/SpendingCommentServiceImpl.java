package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingCommentMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.repository.SpendingCommentRepository;
import com.splitspendings.groupexpensesbackend.service.*;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Validator;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SpendingCommentServiceImpl implements SpendingCommentService {

    private final SpendingCommentRepository spendingCommentRepository;

    private final SpendingService spendingService;
    private final AppUserService appUserService;
    private final GroupMembershipService groupMembershipService;
    private final IdentityService identityService;

    private final SpendingCommentMapper spendingCommentMapper;

    private final Validator validator;

    @Override
    public SpendingCommentDto spendingCommentById(Long commentId) {
        SpendingComment spendingComment = spendingCommentModelById(commentId);
        verifyCurrentUserActiveMembership(spendingComment);

        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    @Override
    @Transactional
    public SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto) {
        ValidatorUtil.validate(validator, newSpendingCommentDto);

        Spending spending = spendingService.spendingModelById(newSpendingCommentDto.getSpendingID());

        Long groupId = spending.getAddedByGroupMembership().getGroup().getId();
        groupMembershipService.verifyCurrentUserActiveMembership(groupId);

        String message = newSpendingCommentDto.getMessage();
        AppUser appUser = appUserService.appUserModelById(identityService.currentUserID());

        SpendingComment spendingComment = new SpendingComment(message, spending, appUser);
        spendingCommentRepository.save(spendingComment);

        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    @Override
    public SpendingCommentDto updateSpendingCommentInfo(Long id, UpdateSpendingCommentDto updateSpendingCommentDto) {
        ValidatorUtil.validate(validator, updateSpendingCommentDto);

        SpendingComment spendingComment = spendingCommentModelById(id);
        verifyCurrentUserActiveMembership(spendingComment);

        spendingCommentMapper.copyUpdateSpendingCommentDtoToSpendingComment(updateSpendingCommentDto, spendingComment);
        spendingCommentRepository.save(spendingComment);

        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    private SpendingComment spendingCommentModelById(Long id){
        return spendingCommentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Spending comment with id = {%d} not found", id)));
    }

    private void verifyCurrentUserActiveMembership(SpendingComment spendingComment){
        Long groupId = spendingComment.getSpending().getAddedByGroupMembership().getId();
        groupMembershipService.verifyCurrentUserActiveMembership(groupId);
    }
}