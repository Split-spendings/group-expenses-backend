package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.NewAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserSettingsMapper {

    AppUserSettingsWithIdDto appUserSettingsToAppUserSettingsWithIdDto(AppUserSettings appUserSettings);

    AppUserSettingsDto appUserSettingsToAppUserSettingsDto(AppUserSettings appUserSettings);

    AppUserSettings newAppUserSettingsDtoToAppUserSettings(NewAppUserSettingsDto newAppUserSettingsDto);
}
