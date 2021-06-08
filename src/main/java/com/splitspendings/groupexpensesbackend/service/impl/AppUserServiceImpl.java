package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appUser.AppUserDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.repository.AppUserRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

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
}
