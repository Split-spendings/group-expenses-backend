package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMembershipSettingsMapper {
    GroupMembershipSettingsInfoDto groupMembershipSettingsToGroupMembershipSettingsInfoDto(GroupMembershipSettings groupMembershipSettings);
    GroupMembershipSettings copyUpdateGroupMembershipSettingsInfoDtoToGroup(UpdateGroupMembershipSettingsInfoDto updateGroupMembershipSettingsInfoDto, @MappingTarget GroupMembershipSettings groupMembershipSettings);
}
