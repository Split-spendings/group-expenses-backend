package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;

public interface GroupMembershipSettingsService {

    GroupMembershipSettings createDefaultGroupMembershipSettings();

    GroupMembershipSettings createAndSaveDefaultGroupMembershipSettingsForGroupMembership(GroupMembership groupMembership);

    GroupMembershipSettings groupMembershipSettingsModelById(Long id);

    GroupMembershipSettingsDto groupMembershipSettingsById(Long id);

    GroupMembershipSettingsDto updateGroupMembershipSettings(Long id, UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto);
}
