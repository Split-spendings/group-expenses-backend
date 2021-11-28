package com.splitspendings.groupexpensesbackend.dto.appuser.settings;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.Language;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.model.enums.Theme;
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
