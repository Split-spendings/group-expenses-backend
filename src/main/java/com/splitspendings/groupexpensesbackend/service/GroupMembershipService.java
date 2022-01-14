package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteCodeDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import java.util.UUID;

public interface GroupMembershipService {

    GroupMembership groupMembershipModelById(Long id);

    GroupMembership groupActiveMembershipModelByGroupId(UUID appUserId, Long groupId);

    GroupMembership createOrUpdateGroupMembershipForCurrentUser(Group group);

    GroupInviteCodeDto createGroupInviteCode(Long groupId);

    boolean isAppUserActiveMemberOfGroup(UUID appUserId, Long groupId);

    boolean isAdminOfGroup(UUID appUserId, Long groupId);

    void verifyUserActiveMembershipByGroupId(UUID appUserId, Long groupId);

    void verifyCurrentUserActiveMembershipByGroupId(Long groupId);

    void verifyCurrentUserActiveMembershipById(Long id);

    GroupDto joinGroup(String inviteCode);
}
