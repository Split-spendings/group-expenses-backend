package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.NewSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentDto;
import com.splitspendings.groupexpensesbackend.dto.spendingcomment.UpdateSpendingCommentDto;
import com.splitspendings.groupexpensesbackend.mapper.SpendingCommentMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.repository.SpendingCommentRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.service.SpendingCommentService;
import com.splitspendings.groupexpensesbackend.service.SpendingService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
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

    private final Validator validator;

    private final SpendingCommentRepository spendingCommentRepository;

    private final SpendingCommentMapper spendingCommentMapper;

    private final SpendingService spendingService;
    private final AppUserService appUserService;
    private final GroupMembershipService groupMembershipService;
    private final IdentityService identityService;

    /**
     * @param id
     *         id of a spending to be returned
     *
     * @return valid {@link SpendingComment}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link SpendingComment} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to access Spending with given
     *         id
     */
    @Override
    public SpendingComment spendingCommentModelById(Long id) {
        return spendingCommentRepository.findById(id)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("Spending comment with id = {%d} not found", id)));
    }

    /**
     * @param id
     *         id of a spending to be returned
     *
     * @return valid {@link SpendingCommentDto}
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link SpendingComment} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to access {@link
     *         SpendingComment} with given id
     */
    @Override
    public SpendingCommentDto spendingCommentById(Long id) {
        SpendingComment spendingComment = spendingCommentModelById(id);
        verifyCurrentUserActiveMembershipBySpendingComment(spendingComment);
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    /**
     * @param newSpendingCommentDto
     *         data to be saved in the database
     *
     * @throws ConstraintViolationException
     *         when provided DTO doesn't meet requirements
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link Spending} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to access {@link Spending} with
     *         given id
     */
    @Override
    public SpendingCommentDto createSpendingComment(NewSpendingCommentDto newSpendingCommentDto) {
        ValidatorUtil.validate(validator, newSpendingCommentDto);

        Spending spending = spendingService.spendingModelById(newSpendingCommentDto.getSpendingId());
        AppUser currentAppUser = appUserService.appUserModelById(identityService.currentUserID());

        verifyUserActiveMembershipBySpendingAndAppUserId(spending, currentAppUser.getId());

        SpendingComment spendingComment = new SpendingComment();
        spendingComment.setSpending(spending);
        spendingComment.setMessage(newSpendingCommentDto.getMessage());
        spendingComment.setAddedByAppUser(currentAppUser);

        spendingCommentRepository.save(spendingComment);
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    /**
     * @param id
     *         id of a {@link SpendingComment} do be updated
     * @param updateSpendingCommentDto
     *         data to update {@link SpendingComment} with
     *
     * @return {@link SpendingCommentDto} with updated data
     *
     * @throws ConstraintViolationException
     *         when {@link UpdateSpendingCommentDto} doesn't meet requirements
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} when there is no {@link SpendingComment} with given id
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#FORBIDDEN} when current user has no rights to update {@link
     *         SpendingComment} with given id
     */
    @Override
    public SpendingCommentDto updateSpendingComment(Long id, UpdateSpendingCommentDto updateSpendingCommentDto) {
        ValidatorUtil.validate(validator, updateSpendingCommentDto);

        SpendingComment spendingComment = spendingCommentModelById(id);
        UUID currentAppUserId = identityService.currentUserID();

        verifyHasRightsToUpdateComment(currentAppUserId, spendingComment);

        spendingCommentMapper.copyUpdateSpendingCommentDtoToSpendingComment(updateSpendingCommentDto, spendingComment);
        spendingComment.setLastModifiedByAppUser(appUserService.appUserModelById(currentAppUserId));

        spendingCommentRepository.save(spendingComment);
        return spendingCommentMapper.spendingCommentToSpendingCommentDto(spendingComment);
    }

    private void verifyCurrentUserActiveMembershipBySpendingComment(SpendingComment spendingComment) {
        Long groupId = spendingComment.getSpending().getAddedByGroupMembership().getGroup().getId();
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(groupId);
    }

    private void verifyUserActiveMembershipBySpendingAndAppUserId(Spending spending, UUID appUserId) {
        Long groupId = spending.getAddedByGroupMembership().getGroup().getId();
        groupMembershipService.verifyUserActiveMembershipByGroupId(appUserId, groupId);
    }

    private void verifyHasRightsToUpdateComment(UUID appUserId, SpendingComment spendingComment) {
        Long groupId = spendingComment.getSpending().getAddedByGroupMembership().getGroup().getId();

        GroupMembership groupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(appUserId, groupId);

        boolean isAuthorOfComment = Objects.equals(spendingComment.getAddedByAppUser().getId(), appUserId);
        boolean hasAdminRights = groupMembership.getHasAdminRights();

        if (!(isAuthorOfComment || hasAdminRights)) {
            throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.FORBIDDEN,
                    String.format("User with id = {%s} is not an author of comment with id = {%d} and does not have admin rights",
                            appUserId,
                            spendingComment.getId()));
        }
    }
}