package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.service.GroupInviteService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/groups/invites")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupInviteController {

    private final GroupInviteService groupInviteService;
    private final GroupMembershipService groupMembershipService;
    @GetMapping({"/{id}"})
    public GroupInviteDto getGroupInviteById(@PathVariable Long id) {
        return groupInviteService.groupInviteById(id);
    }

    @PostMapping
    public GroupInviteDto createGroupInvite(@RequestBody NewGroupInviteDto newGroupInviteDto) {
        return groupInviteService.createGroupInvite(newGroupInviteDto);
    }

    @PostMapping("/generate/{groupId}")
    public String createGroupInviteCode(@PathVariable Long groupId) {
        return groupMembershipService.createGroupInviteCode(groupId);
    }

    @PatchMapping("/{id}")
    public GroupInviteAcceptedDto acceptGroupInvite(@PathVariable Long id) {
        return groupInviteService.acceptGroupInvite(id);
    }

    @DeleteMapping("/{id}")
    public void declineGroupInvite(@PathVariable Long id) {
        groupInviteService.declineGroupInvite(id);
    }
}
