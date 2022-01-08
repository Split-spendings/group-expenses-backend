package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipShortDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipWithIdShortDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMemberDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AppUserMapper.class, GroupMapper.class})
public interface GroupMembershipMapper {

    GroupMemberDto groupMembershipToGroupMemberDto(GroupMembership groupMembership);

    List<GroupMemberDto> groupMembershipListToGroupMemberDtoList(List<GroupMembership> groupMembershipList);

    GroupMembershipDto groupMembershipToGroupMembershipDto(GroupMembership groupMembership);

    GroupMembershipShortDto groupMembershipToGroupMembershipShortDto(GroupMembership groupMembership);

    GroupMembershipWithIdShortDto groupMembershipToGroupMembershipWithIdShortDto(GroupMembership groupMembership);
}
