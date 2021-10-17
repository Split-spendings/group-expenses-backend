package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
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

    @Override
    public GroupMembership groupMembershipModelById(Long id) {
        return groupMembershipRepository.findById(id).orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, "Group membership not found"));
    }

    @Override
    public GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an active member of a group"));
    }

    @Override
    public boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId).isPresent();
    }

    @Override
    public boolean isAdminOfGroup(UUID appUserId, Long groupId){
        GroupMembership groupMembership = groupActiveMembershipModelByGroupId(appUserId, groupId);
        return groupMembership.getHasAdminRights();
    }

    @Override
    public void verifyUserActiveMembershipByGroupId(UUID appUserId, Long groupId) {
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            String logMessage = String.format("User with id = {%s} is not an active member of a Group with id = {%d}",
                    appUserId.toString(),
                    groupId);
            log.info(logMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, logMessage);
        }
    }

    @Override
    public void verifyCurrentUserActiveMembershipByGroupId(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        verifyUserActiveMembershipByGroupId(appUserId, groupId);
    }

    @Override
    public void verifyCurrentUserActiveMembershipById(Long id) {
        UUID appUserId = identityService.currentUserID();
        GroupMembership groupMembership = groupMembershipModelById(id);

        if(!Objects.equals(groupMembership.getAppUser().getId(), appUserId)){
            String logMessage = String.format("User with id = {%s} does not belong to GroupMembership with id = {%d}",
                    appUserId.toString(),
                    groupMembership.getId());
            log.info(logMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, logMessage);
        }

        if(!groupMembership.getActive()) {
            String logMessage = String.format("User with id = {%s} is not active member of GroupMembership with id = {%d}",
                    appUserId.toString(),
                    groupMembership.getId());
            log.info(logMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, logMessage);
        }
    }
}
