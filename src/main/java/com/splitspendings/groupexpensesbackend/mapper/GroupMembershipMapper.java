package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipShortDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipWithIdShortDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, GroupMapper.class})
public interface GroupMembershipMapper {

    GroupMembershipDto groupMembershipToGroupMembershipDto(GroupMembership groupMembership);

    GroupMembershipShortDto groupMembershipToGroupMembershipShortDto(GroupMembership groupMembership);

    GroupMembershipWithIdShortDto groupMembershipToGroupMembershipWithIdShortDto(GroupMembership groupMembership);
}
