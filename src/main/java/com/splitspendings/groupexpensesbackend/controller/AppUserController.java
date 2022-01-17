package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoWithSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserReceivedGroupInvitesDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.NewAppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.UpdateLoginNameDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.UpdateAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public AppUserFullInfoDto profile() {
        return appUserService.profile();
    }

    @GetMapping("/current")
    public AppUserDto profileShort() {
        return appUserService.profileShort();
    }


    @GetMapping("/settings")
    public AppUserSettingsWithIdDto settings() {
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

    @PatchMapping("/login-name")
    public AppUserFullInfoDto updateAppUserLoginName(@RequestBody UpdateLoginNameDto updateLoginNameDto) {
        return appUserService.updateAppUserLoginName(updateLoginNameDto);
    }

    @PatchMapping("/settings")
    public AppUserSettingsWithIdDto updateAppUserSettings(@RequestBody UpdateAppUserSettingsDto updateAppUserSettingsDto) {
        return appUserService.updateAppUserSettings(updateAppUserSettingsDto);
    }

    @GetMapping("/invites")
    public AppUserReceivedGroupInvitesDto appUserReceivedGroupInvites() {
        return appUserService.appUserReceivedGroupInvites();
    }
}
