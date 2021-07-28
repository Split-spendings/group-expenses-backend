package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.NewAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.UpdateAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppUserSettingsMapper {

    AppUserSettingsWithIdDto appUserSettingsToAppUserSettingsWithIdDto(AppUserSettings appUserSettings);

    AppUserSettingsDto appUserSettingsToAppUserSettingsDto(AppUserSettings appUserSettings);

    AppUserSettings newAppUserSettingsDtoToAppUserSettings(NewAppUserSettingsDto newAppUserSettingsDto);

    AppUserSettings copyFromUpdateAppUserSettingsDtoToAppUserSettings(UpdateAppUserSettingsDto updateAppUserSettingsDto, @MappingTarget AppUserSettings appUserSettings);
}
