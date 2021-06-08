package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.appUser.AppUserDto;
import com.splitspendings.groupexpensesbackend.model.AppUser;

import java.util.UUID;

public interface AppUserService {

    AppUser appUserModelById(UUID id);

    AppUser appUserModelByLoginName(String loginName);

    AppUserDto appUserById(UUID id);

    AppUserDto appUserByLoginName(String loginName);
}
