package com.splitspendings.groupexpensesbackend.controller;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final GroupService groupService;

    @GetMapping("/{id}")
    public GroupInfoDto groupInfo(@PathVariable Long id) {
        return groupService.groupInfoById(id);
    }

    @PostMapping
    public GroupInfoDto createGroup(@RequestBody NewGroupDto newGroupDto) {
        return groupService.createGroup(newGroupDto);
    }
}
