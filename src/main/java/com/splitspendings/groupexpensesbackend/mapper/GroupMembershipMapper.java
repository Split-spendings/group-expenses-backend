package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMembershipMapper {

    GroupMembershipDto groupMembershipToGroupMembershipDto(GroupMembership groupMembership);
}
