package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Validator validator;

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;

    private final GroupMapper groupMapper;

    private final AppUserService appUserService;


    private void trimAndValidateNewGroupDto(NewGroupDto newGroupDto) {
        newGroupDto.trim();

        Set<ConstraintViolation<NewGroupDto>> violations = validator.validate(newGroupDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public Group groupModelById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return group.get();
    }

    @Override
    public GroupInfoDto groupInfoById(Long id) {
        return groupMapper.groupToGroupInfoDto(groupModelById(id));
    }

    @Override
    public GroupInfoDto createGroup(NewGroupDto newGroupDto) {
        trimAndValidateNewGroupDto(newGroupDto);

        //UUID currentAppUserId = identityService.currentUserID();
        UUID currentAppUserId = newGroupDto.getOwnerId();
        AppUser currentAppUser = appUserService.appUserModelById(currentAppUserId);

        Group newGroup = groupMapper.newGroupDtoToGroup(newGroupDto);
        newGroup.setOwner(currentAppUser);

        Group createdGroup = groupRepository.save(newGroup);

        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setGroup(createdGroup);
        groupMembership.setAppUser(currentAppUser);
        groupMembership.setActive(true);
        groupMembership.setHasAdminRights(true);
        groupMembership.setFirstTimeJoined(groupMembership.getTimeCreated());
        groupMembership.setLastTimeJoined(groupMembership.getTimeCreated());

        groupMembershipRepository.save(groupMembership);

        return groupMapper.groupToGroupInfoDto(createdGroup);
    }
}
