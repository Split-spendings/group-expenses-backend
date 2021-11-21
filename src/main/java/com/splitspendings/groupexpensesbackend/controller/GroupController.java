package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{id}")
    public GroupInfoDto groupInfo(@PathVariable Long id) {
        return groupService.groupInfoById(id);
    }

    @PostMapping
    public GroupInfoDto createGroup(@RequestBody NewGroupDto newGroupDto) {
        return groupService.createGroup(newGroupDto);
    }

    @PatchMapping("/{id}")
    public GroupInfoDto updateGroupInfo(@PathVariable Long id, @RequestBody UpdateGroupInfoDto updateGroupInfoDto) {
        return groupService.updateGroupInfo(id, updateGroupInfoDto);
    }

    @GetMapping("/{id}/members")
    public GroupActiveMembersDto groupActiveMembers(@PathVariable Long id) {
        return groupService.groupActiveMembersById(id);
    }

    @GetMapping("/{id}/members/{appUserId}")
    public GroupMembershipDto groupMembership(@PathVariable Long id, @PathVariable UUID appUserId) {
        return groupService.groupMembership(id, appUserId);
    }

    @PatchMapping("/{id}/leave")
    public void leaveGroup(@PathVariable Long id) {
        groupService.leaveGroup(id);
    }

    @GetMapping("/{id}/spendings")
    public GroupSpendingsDto groupSpendings(@PathVariable Long id) {
        return groupService.groupSpendings(id);
    }
}
