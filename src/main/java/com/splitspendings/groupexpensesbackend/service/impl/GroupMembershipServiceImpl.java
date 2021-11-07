package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;

    private final IdentityService identityService;

    /**
     * @param id
     *         id of a {@link GroupMembership} to be found
     *
     * @return {@link GroupMembership}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if there is no {@link GroupMembership} with given id in the
     *         database
     */
    @Override
    public GroupMembership groupMembershipModelById(Long id) {
        return groupMembershipRepository.findById(id).
                orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("GroupMembership with id = {%d} not found", id)));
    }

    /**
     * Finds {@link GroupMembership} based on appUserId and groupId
     *
     * @param appUserId
     *         id of {@link AppUser} to be checked
     * @param groupId
     *         id of {@link Group} to be checked
     *
     * @return {@link GroupMembership} with {GroupMembership#active} set to true
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if there is no {@link GroupMembership} with given {@param
     *         appUserId} and {@param groupId} and {GroupMembership#active} set to true
     */
    @Override
    public GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("User with id = {%s} is not an active member of a Group with id = {%d}",
                            appUserId.toString(),
                            groupId)));
    }

    /**
     * @param appUserId
     *         id of {@link AppUser} to be checked
     * @param groupId
     *         id of {@link Group} to be checked
     *
     * @return true if there is a {@link GroupMembership} with given {@param appUserId} and {@param groupId} and
     * {GroupMembership#active} set to true, false otherwise
     */
    @Override
    public boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId).isPresent();
    }

    /**
     * @param appUserId
     *         id of {@link AppUser} to be checked
     * @param groupId
     *         id of {@link Group} to be checked
     *
     * @return true if {@link AppUser} has admin rights on {@link Group}, false otherwise
     *
     */
    @Override
    public boolean isAdminOfGroup(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId)
                .map(GroupMembership::getHasAdminRights).orElse(false);
    }

    /**
     * Checks whether given {@link AppUser} is an active member of {@link Group}
     *
     * @param appUserId
     *         id of {@link AppUser} to be checked
     * @param groupId
     *         id of {@link Group} to be checked
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if {@link AppUser} is not an active member of {@link
     *         Group}
     */
    @Override
    public void verifyUserActiveMembershipByGroupId(UUID appUserId, Long groupId) {
        if (!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.FORBIDDEN,
                    String.format("User with id = {%s} is not an active member of a Group with id = {%d}",
                        appUserId.toString(),
                        groupId));
        }
    }

    /**
     * Checks whether current {@link AppUser} is an active member of {@link Group}
     *
     * @param groupId
     *         id of {@link Group} to be checked
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current {@link AppUser} is not an active member of
     *         {@link Group}
     */
    @Override
    public void verifyCurrentUserActiveMembershipByGroupId(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        verifyUserActiveMembershipByGroupId(appUserId, groupId);
    }

    /**
     * Checks whether current {@link AppUser} is an active member of {@link GroupMembership}
     *
     * @param id
     *         id of {@link GroupMembership} to be checked
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if there is no {@link GroupMembership} with given id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current {@link AppUser} is not active member of {@link
     *         GroupMembership}
     */
    @Override
    public void verifyCurrentUserActiveMembershipById(Long id) {
        UUID appUserId = identityService.currentUserID();
        GroupMembership groupMembership = groupMembershipModelById(id);

        if (!Objects.equals(groupMembership.getAppUser().getId(), appUserId)) {
            throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.FORBIDDEN,
                    String.format("User with id = {%s} does not belong to GroupMembership with id = {%d}",
                            appUserId.toString(),
                            groupMembership.getId()));
        }

        if (!groupMembership.getActive()) {
            throw LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.FORBIDDEN,
                    String.format("User with id = {%s} is not active member of GroupMembership with id = {%d}",
                        appUserId.toString(),
                        groupMembership.getId()));
        }
    }
}
