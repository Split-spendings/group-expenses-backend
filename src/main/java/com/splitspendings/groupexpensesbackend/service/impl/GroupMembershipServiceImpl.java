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
    public GroupMembership groupMembershipModel(UUID appUserId, Long groupID) {
        Optional<GroupMembership> groupMembership = groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId);
        if (groupMembership.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not a member of a group");
        }
        return groupMembership.get();
    }

    @Override
    public boolean isAppUserMemberOfGroup(UUID appUserId, Long groupID) {
        return groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(groupID, appUserId).isPresent();
    }

    @Override
    public void verifyMembership(UUID appUserId, Long groupId) {
        if(!isAppUserMemberOfGroup(appUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of a group");
        }
    }

    @Override
    public void verifyCurrentUserMembership(Long groupId) {
        UUID appUserId = identityService.currentUserID();
        verifyMembership(appUserId, groupId);
    }
}
