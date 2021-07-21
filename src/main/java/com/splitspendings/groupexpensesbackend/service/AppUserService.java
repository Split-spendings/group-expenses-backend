package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.appuser.*;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;

import java.util.List;
import java.util.UUID;

public interface AppUserService {

    List<AppUserDto> allAppUsers();

    AppUser appUserModelById(UUID id);

    AppUserSettings appUserSettingsModelById(UUID id);

    AppUser appUserModelByLoginName(String loginName);

    AppUserDto appUserById(UUID id);

    AppUserDto appUserByLoginName(String loginName);

    AppUser synchroniseWithIdentity();

    AppUserFullInfoDto profile();

    AppUserSettingsWithIdDto settings();

    AppUserFullInfoWithSettingsDto createAppUser(NewAppUserDto newAppUserDto);

    AppUserGroupsDto appUserGroups();
}
