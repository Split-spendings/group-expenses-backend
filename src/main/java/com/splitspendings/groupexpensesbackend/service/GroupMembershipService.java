package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;

import java.util.UUID;

public interface GroupMembershipService {

    GroupMembership groupMembershipModelById(Long id);

    GroupMembership groupActiveMembershipModel(UUID appUserId, Long groupID);

    boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupID);

    void verifyActiveMembership(UUID appUserId, Long groupId);

    void verifyCurrentUserActiveMembership(Long groupId);

    GroupMembershipSettings createDefaultGroupMembershipSettings();

    GroupMembershipSettings createAndSaveDefaultGroupMembershipSettingsForGroupMembership(GroupMembership groupMembership);
}
