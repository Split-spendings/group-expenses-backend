package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoWithSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserIdentityDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.NewAppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.UpdateLoginNameDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto appUserToAppUserDto(AppUser appUser);

    List<AppUserDto> appUserListToAppUserDtoList(List<AppUser> appUserList);

    AppUser appUserDtoToAppUser(AppUserDto appUserDto);

    AppUser newAppUserDtoToAppUser(NewAppUserDto newAppUserDto);

    AppUser copyFromAppUserIdentityDtoToAppUser(AppUserIdentityDto updateLoginNameDto, @MappingTarget AppUser appUser);

    AppUserFullInfoDto appUserToAppUserFullInfoDto(AppUser appUser);

    AppUserFullInfoWithSettingsDto appUserToAppUserFullInfoWithSettingsDto(AppUser appUser);

    AppUser copyFromUpdateLoginNameDtoToAppUser(UpdateLoginNameDto updateLoginNameDto, @MappingTarget AppUser appUser);
}
