package com.splitspendings.groupexpensesbackend.dto.appusersettings;

import com.splitspendings.groupexpensesbackend.model.enums.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class NewAppUserSettingsDto {

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
