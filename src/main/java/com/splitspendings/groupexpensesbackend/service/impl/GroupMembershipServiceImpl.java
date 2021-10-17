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
    public GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an active member of a group"));
    }

    @Override
    public boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupId) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupId, appUserId).isPresent();
    }

    @Override
    public void verifyCurrentUserActiveMembershipByGroupId(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }

    @Override
    public void verifyCurrentUserActiveMembership(Long id) {
        UUID appUserId = identityService.currentUserID();
        GroupMembership groupMembership = groupMembershipModelById(id);
        if(!isAppUserActiveMember(appUserId, groupMembership)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }

    private boolean isAppUserActiveMember(UUID appUserId, GroupMembership groupMembership) {
        return groupMembership.getActive() && groupMembership.getAppUser().getId().equals(appUserId);
    }
}
