package com.splitspendings.groupexpensesbackend.mapper;

import com.splitspendings.groupexpensesbackend.dto.appUser.AppUserDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUserDto appUserToAppUserDto(AppUser appUser);
}
