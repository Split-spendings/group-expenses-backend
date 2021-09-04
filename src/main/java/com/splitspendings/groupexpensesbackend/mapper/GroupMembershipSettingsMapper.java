package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMembershipSettingsMapper {
    GroupMembershipSettingsInfoDto groupMembershipSettingsToGroupMembershipSettingsInfoDto(GroupMembershipSettings groupMembershipSettings);
}
