package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.enums.GroupFilter;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
import com.splitspendings.groupexpensesbackend.model.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    Group groupModelById(Long id);

    GroupDto groupById(Long id);

    GroupDto createGroup(NewGroupDto newGroupDto);

    GroupDto updateGroup(Long id, UpdateGroupDto updateGroupDto);

    List<GroupDto> getAllGroupsFilterBy(GroupFilter groupFilter);

    GroupActiveMembersDto groupActiveMembersById(Long id);

    GroupMembershipDto groupMembership(Long id, UUID appUserId);

    void leaveGroup(Long id);

    GroupSpendingsDto groupSpendings(Long id);

    Iterable<PayoffDto> groupPayoffs(Long id);
}
