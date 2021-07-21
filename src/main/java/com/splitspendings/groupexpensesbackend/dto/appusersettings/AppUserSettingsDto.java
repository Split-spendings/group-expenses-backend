package com.splitspendings.groupexpensesbackend.dto.appusersettings;

import com.splitspendings.groupexpensesbackend.model.enums.*;
import lombok.Data;

import java.util.Set;

@Data
public class AppUserSettingsDto {

    private Language language;
    private Theme theme;
    private Currency defaultCurrency;
    private NotificationOption notificationOption;
    private GroupInviteOption groupInviteOption;
    private Set<NotificationCategory> notificationCategories;
}
