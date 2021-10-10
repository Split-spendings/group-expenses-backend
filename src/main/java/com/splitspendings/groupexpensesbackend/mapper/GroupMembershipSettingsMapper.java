package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMembershipSettingsMapper {

    GroupMembershipSettingsDto groupMembershipSettingsToGroupMembershipSettingsDto(GroupMembershipSettings groupMembershipSettings);

    GroupMembershipSettings copyUpdateGroupMembershipSettingsDtoToGroupMembershipSettings(UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto, @MappingTarget GroupMembershipSettings groupMembershipSettings);
}