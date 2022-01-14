package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import com.splitspendings.groupexpensesbackend.model.enums.GroupTheme;
import com.splitspendings.groupexpensesbackend.model.enums.NotificationOption;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipSettingsRepository;
import com.splitspendings.groupexpensesbackend.service.DefaultGroupMembershipSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DefaultGroupMembershipSettingsServiceImpl implements DefaultGroupMembershipSettingsService {

    private final GroupMembershipSettingsRepository groupMembershipSettingsRepository;

    /**
     * Creates {@link GroupMembershipSettings} with default values
     *
     * @return created {@link GroupMembershipSettings}
     */
    @Override
    public GroupMembershipSettings createDefaultGroupMembershipSettings() {
        GroupMembershipSettings groupMembershipSettings = new GroupMembershipSettings();
        groupMembershipSettings.setGroupTheme(GroupTheme.DEFAULT);
        groupMembershipSettings.setNotificationOption(NotificationOption.ALL);
        return groupMembershipSettings;
    }

    /**
     * Creates and saves default {@link GroupMembershipSettings}
     *
     * @param groupMembership
     *         {@link GroupMembership} to be used for saving default {@link GroupMembershipSettings}
     *
     * @return newly created {@link GroupMembershipSettings}
     */
    @Override
    public GroupMembershipSettings createAndSaveDefaultGroupMembershipSettingsForGroupMembership(GroupMembership groupMembership) {
        GroupMembershipSettings defaultGroupMembershipSettings = createDefaultGroupMembershipSettings();
        defaultGroupMembershipSettings.setGroupMembership(groupMembership);
        return groupMembershipSettingsRepository.save(defaultGroupMembershipSettings);
    }
}