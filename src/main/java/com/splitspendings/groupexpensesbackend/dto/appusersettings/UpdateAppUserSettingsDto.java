package com.splitspendings.groupexpensesbackend.dto.appusersettings;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.GroupInviteOption;
import com.splitspendings.groupexpensesbackend.model.enums.Language;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationCategory;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.model.enums.Theme;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UpdateAppUserSettingsDto {

    @NotNull
    private Language language;
    @NotNull
    private Theme theme;
    @NotNull
    private Currency defaultCurrency;
    @NotNull
    private NotificationOption notificationOption;
    @NotNull
    private GroupInviteOption groupInviteOption;
    @NotNull
    private Set<NotificationCategory> notificationCategories;
}
