package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.*;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.AppUserSettingsWithIdDto;
import com.splitspendings.groupexpensesbackend.dto.appusersettings.UpdateAppUserSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.AppUserSettingsMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupInviteMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.AppUserSettings;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.repository.AppUserSettingsRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    private final Validator validator;

    private final AppUserRepository appUserRepository;
    private final AppUserSettingsRepository appUserSettingsRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    private final AppUserMapper appUserMapper;
    private final AppUserSettingsMapper appUserSettingsMapper;
    private final GroupMapper groupMapper;
    private final GroupInviteMapper groupInviteMapper;

    private final IdentityService identityService;

    @Override
    public List<AppUserDto> allAppUsers() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return appUserMapper.appUserListToAppUserDtoList(appUsers);
    }

    @Override
    public AppUser appUserModelById(UUID id) {
        Optional<AppUser> appUser = appUserRepository.findById(id);
        if (appUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return appUser.get();
    }

    @Override
    public AppUserSettings appUserSettingsModelById(UUID id) {
        Optional<AppUserSettings> appUserSettings = appUserSettingsRepository.findById(id);
        if (appUserSettings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return appUserSettings.get();
    }

    @Override
    public AppUser appUserModelByLoginName(String loginName) {
        Optional<AppUser> appUser = appUserRepository.findByLoginName(loginName);
        if (appUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return appUser.get();
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
        newAppUserDto.trim();

        Set<ConstraintViolation<NewAppUserDto>> violations = validator.validate(newAppUserDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

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
    public AppUserGroupsDto appUserActiveGroups() {
        UUID currentUserId = identityService.currentUserID();
        List<Group> appUserGroups = groupMembershipRepository.queryGroupsWithAppUserActiveMembership(currentUserId);
        List<GroupInfoDto> groupInfoDtoList = groupMapper.groupListToGroupInfoDtoList(appUserGroups);
        AppUserGroupsDto appUserGroupsDto = new AppUserGroupsDto();
        appUserGroupsDto.setId(currentUserId);
        appUserGroupsDto.setGroups(groupInfoDtoList);
        return appUserGroupsDto;
    }

    @Override
    public AppUserFullInfoDto updateAppUserLoginName(UpdateLoginNameDto updateLoginNameDto) {
        updateLoginNameDto.trim();

        Set<ConstraintViolation<UpdateLoginNameDto>> violations = validator.validate(updateLoginNameDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        UUID id = identityService.currentUserID();

        Optional<AppUser> appUserOptionalByLoginName = appUserRepository.findByLoginName(updateLoginNameDto.getLoginName());
        if (appUserOptionalByLoginName.isPresent()) {
            if(appUserOptionalByLoginName.get().getId().equals(id)) {
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
        Set<ConstraintViolation<UpdateAppUserSettingsDto>> violations = validator.validate(updateAppUserSettingsDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

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
}
