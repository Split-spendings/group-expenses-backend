package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;

import java.util.UUID;

public interface GroupMembershipService {

    GroupMembership groupMembershipModel(UUID appUserId, Long groupID);

    boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupID);

    void verifyActiveMembership(UUID appUserId, Long groupId);

    void verifyCurrentUserActiveMembership(Long groupId);
}
