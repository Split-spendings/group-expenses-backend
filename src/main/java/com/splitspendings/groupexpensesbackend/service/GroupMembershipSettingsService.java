package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;

public interface GroupMembershipSettingsService {
    GroupMembershipSettings groupMembershipSettingsModelById(Long id);

    GroupMembershipSettingsInfoDto groupMembershipSettingsInfoById(Long id);

}
