package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipSettingsMapper;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipSettingsRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
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
public class GroupMembershipSettingsServiceImpl implements GroupMembershipSettingsService {

    private final GroupMembershipSettingsRepository groupMembershipSettingsRepository;

    private final GroupMembershipSettingsMapper groupMembershipSettingsMapper;

    @Override
    public GroupMembershipSettings groupMembershipSettingsModelById(Long id) {
        return groupMembershipSettingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupMembershipSettings not found"));
    }

    @Override
    public GroupMembershipSettingsInfoDto groupMembershipSettingsInfoById(Long id) {
        return groupMembershipSettingsMapper.groupMembershipSettingsToGroupMembershipSettingsInfoDto(groupMembershipSettingsModelById(id));
    }
}
