package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingCommentMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.repository.SpendingCommentRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.SpendingCommentService;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Objects;
import java.util.UUID;

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

    /**
     * @param id
     *      id of a spending to be returned
     * @throws ResponseStatusException with status {@link HttpStatus#NOT_FOUND}
     *      when there is no {@link SpendingComment} with given id
     * @throws ResponseStatusException with status {@link HttpStatus#FORBIDDEN}
     *      when current user has no rights to access Spending with given id
     * @return valid {@link SpendingComment}
     */
    @Override
    public SpendingComment spendingCommentModelById(Long id){
        SpendingComment spendingComment =  spendingCommentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Spending comment with id = {%d} not found", id)));
        verifyCurrentUserActiveMembershipByGroupId(spendingComment);
        return spendingComment;
    }

    /**
     * @param id
     *      id of a spending to be returned
     * @throws ResponseStatusException with status {@link HttpStatus#NOT_FOUND}
     *      when there is no {@link SpendingComment} with given id
     * @throws ResponseStatusException with status {@link HttpStatus#FORBIDDEN}
     *      when current user has no rights to access {@link SpendingComment} with given id
     * @return valid {@link SpendingCommentDto}
     */
    @Override
    public SpendingCommentDto spendingCommentById(Long id) {
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingCommentModelById(id));
    }

    /**
     * @param newSpendingCommentDto
     *      data to be saved in the database
     * @throws ConstraintViolationException
     *      when provided DTO doesn't meet requirements
     * @throws ResponseStatusException with status {@link HttpStatus#NOT_FOUND}
     *       when there is no {@link Spending} with given id
     * @throws ResponseStatusException with status {@link HttpStatus#FORBIDDEN}
     *       when current user has no rights to access {@link Spending} with given id
     *
     */
    @Override
    public SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto) {
        ValidatorUtil.validate(validator, newSpendingCommentDto);

        Spending spending = spendingService.spendingModelById(newSpendingCommentDto.getSpendingId());
        AppUser appUser = appUserService.appUserModelById(identityService.currentUserID());
        Long groupId = spending.getAddedByGroupMembership().getGroup().getId();

        groupMembershipService.verifyUserActiveMembershipByGroupId(appUser.getId(), groupId);
        //todo move check whether user can access Spending to the spendingModelById method

        SpendingComment spendingComment = new SpendingComment();
        spendingComment.setSpending(spending);
        spendingComment.setMessage(newSpendingCommentDto.getMessage());
        spendingComment.setAddedByAppUser(appUser);

        spendingCommentRepository.save(spendingComment);

        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    @Override
    public SpendingCommentDto updateSpendingComment(Long id, UpdateSpendingCommentDto updateSpendingCommentDto) {
        ValidatorUtil.validate(validator, updateSpendingCommentDto);

        SpendingComment spendingComment = spendingCommentModelById(id);
        UUID appUserId = identityService.currentUserID();
        if (!(Objects.equals(spendingComment.getAddedByAppUser().getId(), appUserId)
                || isAdminOfGroup(appUserId, spendingComment))){
            log.info(String.format(
                    "User with id = {%s} is not an author of comment with id = {%d} and does not have admin rights",
                    appUserId.toString(),
                    spendingComment.getId()));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        spendingCommentMapper.copyUpdateSpendingCommentDtoToSpendingComment(updateSpendingCommentDto, spendingComment);
        spendingCommentRepository.save(spendingComment);
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    private void verifyCurrentUserActiveMembershipByGroupId(SpendingComment spendingComment){
        Long groupId = spendingComment.getSpending().getAddedByGroupMembership().getGroup().getId();
        UUID appUserId = spendingComment.getAddedByAppUser().getId();
        groupMembershipService.verifyUserActiveMembershipByGroupId(appUserId, groupId);
    }

    private boolean isAdminOfGroup(UUID appUserId, SpendingComment spendingComment){
        return groupMembershipService.isAdminOfGroup(appUserId, spendingComment.getSpending().getAddedByGroupMembership().getGroup().getId());
    }
}