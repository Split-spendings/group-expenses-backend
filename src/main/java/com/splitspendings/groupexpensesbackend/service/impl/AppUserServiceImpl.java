package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserFullInfoWithSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserIdentityDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserReceivedGroupInvitesDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.NewAppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.UpdateLoginNameDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.settings.UpdateAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.AppUserSettingsMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupInviteMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.repository.AppUserSettingsRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    private final Validator validator;

    private final AppUserRepository appUserRepository;
    private final AppUserSettingsRepository appUserSettingsRepository;

    private final AppUserMapper appUserMapper;
    private final AppUserSettingsMapper appUserSettingsMapper;
    private final GroupInviteMapper groupInviteMapper;

    private final IdentityService identityService;

    @Override
    public List<AppUserDto> allAppUsers() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return appUserMapper.appUserListToAppUserDtoList(appUsers);
    }

    @Override
    public AppUser appUserModelById(UUID id) {
        return appUserRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public AppUserSettings appUserSettingsModelById(UUID id) {
        return appUserSettingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public AppUser appUserModelByLoginName(String loginName) {
        return appUserRepository.findByLoginName(loginName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public AppUserDto appUserById(UUID id) {
        return appUserMapper.appUserToAppUserDto(appUserModelById(id));
    }

    @Override
    public AppUserDto appUserByLoginName(String loginName) {
        return appUserMapper.appUserToAppUserDto(appUserModelByLoginName(loginName));
    }

    @Override
    public AppUser synchroniseWithIdentity() {
        AppUserIdentityDto appUserIdentityDto = identityService.currentUser();
        AppUser existingAppUser = appUserModelById(appUserIdentityDto.getId());
        AppUser synchronizedAppUser = appUserMapper.copyFromAppUserIdentityDtoToAppUser(appUserIdentityDto, existingAppUser);
        return appUserRepository.save(synchronizedAppUser);
    }

    @Override
    public AppUserFullInfoDto profile() {
        AppUser synchronisedAppUser = synchroniseWithIdentity();
        return appUserMapper.appUserToAppUserFullInfoDto(synchronisedAppUser);
    }

    @Override
    public AppUserSettingsWithIdDto settings() {
        UUID currentAppUserId = identityService.currentUserID();
        AppUserSettings appUserSettings = appUserSettingsModelById(currentAppUserId);
        return appUserSettingsMapper.appUserSettingsToAppUserSettingsWithIdDto(appUserSettings);
    }

    @Override
    public AppUserFullInfoWithSettingsDto createAppUser(NewAppUserDto newAppUserDto) {
        ValidatorUtil.validate(validator, newAppUserDto);

        AppUserIdentityDto appUserIdentityDto = identityService.currentUser();

        Optional<AppUser> appUserOptionalById = appUserRepository.findById(appUserIdentityDto.getId());
        if (appUserOptionalById.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        Optional<AppUser> appUserOptionalByLoginName = appUserRepository.findByLoginName(newAppUserDto.getLoginName());
        if (appUserOptionalByLoginName.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login name is used by another user");
        }

        AppUser newAppUser = appUserMapper.newAppUserDtoToAppUser(newAppUserDto);
        newAppUser = appUserMapper.copyFromAppUserIdentityDtoToAppUser(appUserIdentityDto, newAppUser);

        AppUserSettings appUserSettings = appUserSettingsMapper.newAppUserSettingsDtoToAppUserSettings(newAppUserDto.getNewAppUserSettingsDto());
        appUserSettings.setAppUser(newAppUser);

        AppUserSettings createdAppUserSettings = appUserSettingsRepository.save(appUserSettings);

        AppUser createdAppUser = createdAppUserSettings.getAppUser();
        AppUserFullInfoWithSettingsDto appUserFullInfoWithSettingsDto = appUserMapper.appUserToAppUserFullInfoWithSettingsDto(createdAppUser);
        AppUserSettingsDto appUserSettingsDto = appUserSettingsMapper.appUserSettingsToAppUserSettingsDto(createdAppUserSettings);
        appUserFullInfoWithSettingsDto.setAppUserSettingsDto(appUserSettingsDto);

        return appUserFullInfoWithSettingsDto;
    }

    @Override
    public AppUserFullInfoDto updateAppUserLoginName(UpdateLoginNameDto updateLoginNameDto) {
        ValidatorUtil.validate(validator, updateLoginNameDto);

        UUID id = identityService.currentUserID();

        Optional<AppUser> appUserOptionalByLoginName = appUserRepository.findByLoginName(updateLoginNameDto.getLoginName());
        if (appUserOptionalByLoginName.isPresent()) {
            if (appUserOptionalByLoginName.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user already has the provided login name");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login name is used by another user");
            }
        }

        AppUser existingAppUser = appUserModelById(id);

        existingAppUser = appUserMapper.copyFromUpdateLoginNameDtoToAppUser(updateLoginNameDto, existingAppUser);

        AppUser updatedAppUser = appUserRepository.save(existingAppUser);
        return appUserMapper.appUserToAppUserFullInfoDto(updatedAppUser);
    }

    @Override
    public AppUserSettingsWithIdDto updateAppUserSettings(UpdateAppUserSettingsDto updateAppUserSettingsDto) {
        ValidatorUtil.validate(validator, updateAppUserSettingsDto);

        UUID id = identityService.currentUserID();

        AppUserSettings existingAppUserSettings = appUserModelById(id).getAppUserSettings();

        existingAppUserSettings = appUserSettingsMapper.copyFromUpdateAppUserSettingsDtoToAppUserSettings(updateAppUserSettingsDto, existingAppUserSettings);

        AppUserSettings updatedAppUserSettings = appUserSettingsRepository.save(existingAppUserSettings);
        return appUserSettingsMapper.appUserSettingsToAppUserSettingsWithIdDto(updatedAppUserSettings);
    }

    @Override
    public AppUserReceivedGroupInvitesDto appUserReceivedGroupInvites() {
        AppUser currentAppUser = appUserModelById(identityService.currentUserID());

        Set<GroupInvite> groupInvites = currentAppUser.getGroupInvitesReceived();
        List<GroupInviteDto> groupInviteDtoList = groupInviteMapper.groupInviteSetToGroupInviteDtoList(groupInvites);

        AppUserReceivedGroupInvitesDto appUserInvites = new AppUserReceivedGroupInvitesDto();
        appUserInvites.setReceivedGroupInvites(groupInviteDtoList);
        return appUserInvites;
    }

    @Override
    public AppUserDto synchroniseAppUser() {
        AppUserIdentityDto currentUserIdentity = identityService.currentUser();
        AppUser userEntity = appUserRepository.getById(currentUserIdentity.getId());
        if(userEntity == null) {
            //TODO create new app user with default settings and data from AppUserIdentityDto (loginName = email)
            userEntity = new AppUser();
        }
        return appUserMapper.appUserToAppUserDto(userEntity);
    }
}
