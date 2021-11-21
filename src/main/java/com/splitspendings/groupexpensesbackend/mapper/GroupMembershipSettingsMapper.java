package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMembershipSettingsMapper {

    GroupMembershipSettingsDto groupMembershipSettingsToGroupMembershipSettingsDto(GroupMembershipSettings groupMembershipSettings);

    GroupMembershipSettings copyUpdateGroupMembershipSettingsDtoToGroupMembershipSettings(UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto, @MappingTarget GroupMembershipSettings groupMembershipSettings);
}
