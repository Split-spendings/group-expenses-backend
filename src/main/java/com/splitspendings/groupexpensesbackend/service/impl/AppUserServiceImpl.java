package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserGroupsDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AppUserRepository appUserRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    private final AppUserMapper appUserMapper;
    private final GroupMapper groupMapper;

    @Override
    public AppUser appUserModelById(UUID id) {
        Optional<AppUser> appUser = appUserRepository.findById(id);
        if (appUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return appUser.get();
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
    public AppUserGroupsDto appUserGroups(UUID id) {
        //UUID currentUserId = identityService.currentUserID();
        UUID currentUserId = id;
        List<Group> appUserGroups = groupMembershipRepository.queryGroupsWithAppUserActiveMembership(currentUserId);
        List<GroupInfoDto> groupInfoDtoList = groupMapper.groupListToGroupInfoDtoList(appUserGroups);
        AppUserGroupsDto appUserGroupsDto = new AppUserGroupsDto();
        appUserGroupsDto.setId(currentUserId);
        appUserGroupsDto.setGroups(groupInfoDtoList);
        return appUserGroupsDto;
    }
}
