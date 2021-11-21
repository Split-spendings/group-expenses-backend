package com.splitspendings.groupexpensesbackend.dto.appuser;

import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsDto;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class AppUserFullInfoWithSettingsDto {

    private UUID id;
    private String loginName;
    private String email;
    private String firstName;
    private String lastName;
    private ZonedDateTime timeRegistered;

    private AppUserSettingsDto appUserSettingsDto;
}
