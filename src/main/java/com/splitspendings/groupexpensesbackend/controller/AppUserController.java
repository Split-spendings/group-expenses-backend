package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.appuser.*;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public AppUserFullInfoDto profile(){
        return appUserService.profile();
    }

    @GetMapping("/settings")
    public AppUserSettingsWithIdDto settings(){
        return appUserService.settings();
    }

    @GetMapping("/{id}")
    public AppUserDto appUserById(@PathVariable UUID id) {
        return appUserService.appUserById(id);
    }

    @GetMapping("/login-name/{loginName}")
    public AppUserDto appUserByLoginName(@PathVariable String loginName) {
        return appUserService.appUserByLoginName(loginName);
    }

    @PostMapping
    public AppUserFullInfoWithSettingsDto createAppUser(@RequestBody NewAppUserDto newAppUserDto) {
        return appUserService.createAppUser(newAppUserDto);
    }

    @GetMapping("/groups")
    public AppUserGroupsDto appUserGroups() {
        return appUserService.appUserGroups();
    }

    @PatchMapping("login-name")
    public AppUserFullInfoDto updateAppUserLoginName(@RequestBody UpdateLoginNameDto updateLoginNameDto) {
        return appUserService.updateAppUserLoginName(updateLoginNameDto);
    }
}
