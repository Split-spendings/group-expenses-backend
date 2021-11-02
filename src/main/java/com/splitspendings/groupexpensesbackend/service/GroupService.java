package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.model.Group;

import java.util.UUID;

public interface GroupService {

    Group groupModelById(Long id);

    GroupInfoDto groupInfoById(Long id);

    GroupInfoDto createGroup(NewGroupDto newGroupDto);

    GroupInfoDto updateGroupInfo(Long id, UpdateGroupInfoDto updateGroupInfoDto);

    GroupActiveMembersDto groupActiveMembersById(Long id);

    GroupMembershipDto groupMembership(Long id, UUID appUserId);

    void leaveGroup(Long id);

    GroupSpendingsDto groupSpendings(Long id);
}
