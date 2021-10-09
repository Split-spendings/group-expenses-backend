package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;

import java.util.UUID;

public interface GroupMembershipService {

    GroupMembership groupMembershipModelById(Long id);

    GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupID);

    boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupID);

    void verifyActiveMembershipByGroupId(UUID appUserId, Long groupId);

    void verifyCurrentUserActiveMembershipByGroupId(Long groupId);

    boolean isAppUserActiveMember(UUID appUserId, GroupMembership groupMembership);

    void verifyActiveMembership(UUID appUserId, GroupMembership groupMembership);

    void verifyCurrentUserActiveMembership(GroupMembership groupMembership);
}
