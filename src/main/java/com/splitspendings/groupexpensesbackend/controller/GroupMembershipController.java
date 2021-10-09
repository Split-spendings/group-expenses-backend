package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembershipsettings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/group-membership")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupMembershipController {

    private final GroupMembershipSettingsService groupMembershipSettingsService;

    @GetMapping("/{id}/settings")
    public GroupMembershipSettingsDto groupMembershipSettings(@PathVariable Long id) {
        return groupMembershipSettingsService.groupMembershipSettingsById(id);
    }

    @PatchMapping("/{id}/settings")
    public GroupMembershipSettingsDto updateGroupMembershipSettings(@PathVariable Long id, @RequestBody UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto) {
        return groupMembershipSettingsService.updateGroupMembershipSettings(id, updateGroupMembershipSettingsDto);
    }
}