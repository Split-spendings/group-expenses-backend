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

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipServiceImpl implements GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;

    private final IdentityService identityService;

    @Override
    public GroupMembership groupActiveMembershipModel(UUID appUserId, Long groupID) {
        Optional<GroupMembership> groupMembership = groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId);
        if (groupMembership.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not an active member of a group");
        }
        return groupMembership.get();
    }

    @Override
    public boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupID) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId).isPresent();
    }

    @Override
    public void verifyActiveMembership(UUID appUserId, Long groupId) {
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not an active member of a group");
        }
    }

    @Override
    public void verifyCurrentUserActiveMembership(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        if(!isAppUserActiveMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not an active member of a group");
        }
    }
}
