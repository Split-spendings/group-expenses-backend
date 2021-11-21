package com.splitspendings.groupexpensesbackend.dto.group.membership.settings;

import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Data;

@Data
public class GroupMembershipSettingsDto {

    private Long id;
    private GroupTheme groupTheme;
    private NotificationOption notificationOption;
}