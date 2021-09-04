package com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings;

import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Data;

@Data
public class GroupMembershipSettingsInfoDto {

    private Long id;
    private GroupTheme groupTheme;
    private NotificationOption notificationOption;
}