package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.enums.GroupFilter;
import com.splitspendings.groupexpensesbackend.dto.group.enums.GroupMembersFilter;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.payoff.PayoffDto;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/groups")
@RequiredArgsConstructor
@SecurityRequirement(name = "identity")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{id}")
    public GroupDto getGroupById(@PathVariable Long id) {
        return groupService.groupById(id);
    }

    @PostMapping
    public GroupDto createGroup(@RequestBody NewGroupDto newGroupDto) {
        return groupService.createGroup(newGroupDto);
    }

    @PatchMapping("/{id}")
    public GroupDto updateGroup(@PathVariable Long id, @RequestBody UpdateGroupDto updateGroupDto) {
        return groupService.updateGroup(id, updateGroupDto);
    }

    @GetMapping("/{id}/members{groupMembersFilter}")
    public GroupMembersDto getFilteredMembers(@PathVariable Long id, @PathVariable GroupMembersFilter groupMembersFilter) {
        return groupService.getFilteredGroupMembers(id, groupMembersFilter);
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

    @GetMapping("/{id}/payoffs")
    public Iterable<PayoffDto> groupPayoffs(@PathVariable Long id) {
        return groupService.groupPayoffs(id);
    }

    @GetMapping("/filter/{groupFilter}")
    public List<GroupDto> getFilteredGroups(@PathVariable GroupFilter groupFilter){
        return groupService.getAllGroupsFilterBy(groupFilter);
    }
}
