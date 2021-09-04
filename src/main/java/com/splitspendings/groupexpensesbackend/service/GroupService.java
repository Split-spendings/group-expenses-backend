package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.*;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;

import java.util.UUID;

public interface GroupService {

    Group groupModelById(Long id);

    GroupInfoDto groupInfoById(Long id);

    GroupInfoDto createGroup(NewGroupDto newGroupDto);

    GroupInfoDto updateGroupInfo(Long id, UpdateGroupInfoDto updateGroupInfoDto);

    GroupActiveMembersDto groupActiveMembersById(Long id);

    GroupMembershipDto groupMembership(Long id, UUID appUserId);

    GroupInviteDto createGroupInvite(NewGroupInviteDto newGroupInviteDto);

    GroupInvite groupInviteModelById(Long inviteId);

    GroupInviteAcceptedDto acceptGroupInvite(Long inviteId);

    void declineGroupInvite(Long id);

    void leaveGroup(Long id);

    GroupSpendingsDto groupSpendings(Long id);
}
