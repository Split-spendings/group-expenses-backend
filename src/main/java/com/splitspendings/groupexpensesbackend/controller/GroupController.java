package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("{id}")
    public GroupInfoDto updateGroupInfo(@PathVariable Long id, @RequestBody UpdateGroupInfoDto updateGroupInfoDto) {
        return groupService.updateGroupInfo(id, updateGroupInfoDto);
    }
}
