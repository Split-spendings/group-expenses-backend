package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;

import java.util.UUID;

public interface GroupMembershipService {

    GroupMembership groupMembershipModel(UUID appUserId, Long groupID);

    boolean isAppUserMemberOfGroup(UUID appUserId, Long groupID);

    void verifyMembership(UUID appUserId, Long groupId);

    void verifyCurrentUserMembership(Long groupId);
}
