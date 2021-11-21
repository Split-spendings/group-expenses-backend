package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.NewAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.UpdateAppUserSettingsDto;
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
