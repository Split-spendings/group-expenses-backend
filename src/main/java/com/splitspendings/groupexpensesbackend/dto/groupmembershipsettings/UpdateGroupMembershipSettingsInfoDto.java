package com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings;

import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateGroupMembershipSettingsInfoDto {
    @NotNull
    private GroupTheme groupTheme;

    @NotNull
    private NotificationOption notificationOption;
}
