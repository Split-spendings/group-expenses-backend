package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoWithSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserGroupsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserReceivedGroupInvitesDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.NewAppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.UpdateLoginNameDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.UpdateAppUserSettingsDto;
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

    AppUserGroupsDto appUserActiveGroups();

    AppUserFullInfoDto updateAppUserLoginName(UpdateLoginNameDto updateLoginNameDto);

    AppUserSettingsWithIdDto updateAppUserSettings(UpdateAppUserSettingsDto updateAppUserSettingsDto);

    AppUserReceivedGroupInvitesDto appUserReceivedGroupInvites();
}
