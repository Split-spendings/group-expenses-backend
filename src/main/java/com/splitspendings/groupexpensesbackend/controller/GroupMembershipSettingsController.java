package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/group-membership-settings")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupMembershipSettingsController {

    private final GroupMembershipSettingsService groupMembershipSettingsService;

    @GetMapping("/{id}")
    public GroupMembershipSettingsInfoDto groupMembershipSettingsInfo(@PathVariable Long id) {
        return groupMembershipSettingsService.groupMembershipSettingsInfoById(id);
    }
}