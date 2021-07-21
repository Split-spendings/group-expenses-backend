package com.splitspendings.groupexpensesbackend.dto.appuser;

import com.splitspendings.groupexpensesbackend.dto.appusersettings.NewAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewAppUserDto {

    @NotNull
    @Size(min = AppUser.NAMES_MIN_LENGTH, max = AppUser.NAMES_MAX_LENGTH)
    private String loginName;

    @NotNull
    @Valid
    private NewAppUserSettingsDto newAppUserSettingsDto;

    public void trim() {
        if (loginName != null) {
            loginName = loginName.trim();
        }
    }
}