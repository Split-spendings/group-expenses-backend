package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;

public interface GroupMembershipSettingsService {

    GroupMembershipSettings groupMembershipSettingsModelById(Long id);

    GroupMembershipSettingsDto groupMembershipSettingsById(Long id);

    GroupMembershipSettingsDto updateGroupMembershipSettings(Long id, UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto);
}
