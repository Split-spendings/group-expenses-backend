package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipSettingsMapper;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipSettingsRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Validator;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipSettingsServiceImpl implements GroupMembershipSettingsService {

    private final Validator validator;

    private final GroupMembershipSettingsRepository groupMembershipSettingsRepository;

    private final GroupMembershipSettingsMapper groupMembershipSettingsMapper;

    private final GroupMembershipService groupMembershipService;

    @Override
    public GroupMembershipSettings createDefaultGroupMembershipSettings() {
        GroupMembershipSettings groupMembershipSettings = new GroupMembershipSettings();
        groupMembershipSettings.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings.setNotificationOption(NotificationOption.ALL);
        return groupMembershipSettings;
    }

    @Override
    public GroupMembershipSettings createAndSaveDefaultGroupMembershipSettingsForGroupMembership(GroupMembership groupMembership) {
        GroupMembershipSettings defaultGroupMembershipSettings = createDefaultGroupMembershipSettings();
        defaultGroupMembershipSettings.setGroupMembership(groupMembership);
        return groupMembershipSettingsRepository.save(defaultGroupMembershipSettings);
    }

    @Override
    public GroupMembershipSettings groupMembershipSettingsModelById(Long id) {
        return groupMembershipSettingsRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupMembershipSettings not found"));
    }

    @Override
    public GroupMembershipSettingsDto groupMembershipSettingsById(Long id) {
        return groupMembershipSettingsMapper.groupMembershipSettingsToGroupMembershipSettingsDto(groupMembershipSettingsModelById(id));
    }

    @Override
    public GroupMembershipSettingsDto updateGroupMembershipSettings(Long id, UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto) {
        ValidatorUtil.validate(validator, updateGroupMembershipSettingsDto);

        GroupMembership groupMembership = groupMembershipService.groupMembershipModelById(id);
        groupMembershipService.verifyCurrentUserActiveMembership(groupMembership);

        GroupMembershipSettings existingGroupMembershipSettings = groupMembership.getGroupMembershipSettings();

        GroupMembershipSettings newGroupMembershipSettings = groupMembershipSettingsMapper.copyUpdateGroupMembershipSettingsDtoToGroupMembershipSettings(updateGroupMembershipSettingsDto, existingGroupMembershipSettings);
        GroupMembershipSettings updatedGroupMembershipSettings = groupMembershipSettingsRepository.save(newGroupMembershipSettings);
        return groupMembershipSettingsMapper.groupMembershipSettingsToGroupMembershipSettingsDto(updatedGroupMembershipSettings);
    }
}