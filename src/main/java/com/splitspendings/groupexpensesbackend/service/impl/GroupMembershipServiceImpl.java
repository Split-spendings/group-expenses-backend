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
    public GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupID) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an active member of a group"));
    }

    @Override
    public boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupID) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId).isPresent();
    }

    @Override
    public void verifyActiveMembershipByGroupId(UUID appUserId, Long groupId) {
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an active member of a group");
        }
    }

    @Override
    public void verifyCurrentUserActiveMembershipByGroupId(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }

    @Override
    public boolean isAppUserActiveMember(UUID appUserId, GroupMembership groupMembership) {
        return groupMembership.getActive() && groupMembership.getAppUser().getId().equals(appUserId);
    }

    @Override
    public void verifyActiveMembership(UUID appUserId, GroupMembership groupMembership) {
        if(!isAppUserActiveMember(appUserId, groupMembership)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an active member of a group");
        }
    }

    @Override
    public void verifyCurrentUserActiveMembership(GroupMembership groupMembership) {
        UUID appUserId = identityService.currentUserID();
        if(!isAppUserActiveMember(appUserId, groupMembership)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }

    @Override
    public boolean isAppUserActiveMember(UUID appUserId, Long id) {
        GroupMembership groupMembership = groupMembershipModelById(id);
        return isAppUserActiveMember(appUserId, groupMembership);
    }

    @Override
    public void verifyActiveMembership(UUID appUserId, Long id) {
        if(!isAppUserActiveMember(appUserId, id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an active member of a group");
        }
    }

    @Override
    public void verifyCurrentUserActiveMembership(Long id) {
        UUID appUserId = identityService.currentUserID();
        if(!isAppUserActiveMember(appUserId, id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }
}
