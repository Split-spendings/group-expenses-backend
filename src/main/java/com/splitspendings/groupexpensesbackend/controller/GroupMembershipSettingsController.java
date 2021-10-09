package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsInfoDto;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}")
    public GroupMembershipSettingsInfoDto updateGroupMembershipSettingsInfo(@PathVariable Long id, @RequestBody UpdateGroupMembershipSettingsInfoDto updateGroupMembershipSettingsInfoDto) {
        return groupMembershipSettingsService.updateGroupMembershipSettingsInfo(id, updateGroupMembershipSettingsInfoDto);
    }
}