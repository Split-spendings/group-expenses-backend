package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteCodeDto;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/groups/invites")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupInviteController {

    private final GroupMembershipService groupMembershipService;

    @PostMapping("/generate/{groupId}")
    public GroupInviteCodeDto generateGroupInviteCode(@PathVariable Long groupId) {
        return groupMembershipService.createGroupInviteCode(groupId);
    }

    @PostMapping("/join/{inviteCode}")
    public GroupDto joinGroupByInviteCode(@PathVariable String inviteCode) {
        return groupMembershipService.joinGroup(inviteCode);
    }
}
