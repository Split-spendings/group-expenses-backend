package com.splitspendings.groupexpensesbackend.dto.group.membership.settings;

import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateGroupMembershipSettingsDto {

    @NotNull
    private GroupTheme groupTheme;

    @NotNull
    private NotificationOption notificationOption;
}
