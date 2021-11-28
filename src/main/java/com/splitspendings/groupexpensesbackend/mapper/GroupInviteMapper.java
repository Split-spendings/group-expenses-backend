package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GroupInviteMapper {

    GroupInviteDto groupInviteToGroupInviteDto(GroupInvite groupInvite);

    List<GroupInviteDto> groupInviteSetToGroupInviteDtoList(Set<GroupInvite> groupInviteList);
}
