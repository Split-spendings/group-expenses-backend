package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupInviteMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.repository.GroupInviteRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final Validator validator;

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupInviteRepository groupInviteRepository;

    private final GroupMapper groupMapper;
    private final AppUserMapper appUserMapper;
    private final GroupMembershipMapper groupMembershipMapper;
    private final GroupInviteMapper groupInviteMapper;

    private final IdentityService identityService;
    private final AppUserService appUserService;
    private final GroupMembershipService groupMembershipService;

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
        newGroupDto.trim();

        Set<ConstraintViolation<NewGroupDto>> violations = validator.validate(newGroupDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        UUID currentAppUserId = identityService.currentUserID();
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

    @Override
    public GroupInfoDto updateGroupInfo(Long id, UpdateGroupInfoDto updateGroupInfoDto) {
        updateGroupInfoDto.trim();

        Set<ConstraintViolation<UpdateGroupInfoDto>> violations = validator.validate(updateGroupInfoDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        groupMembershipService.verifyCurrentUserActiveMembership(id);

        Group existingGroup = groupModelById(id);

        existingGroup = groupMapper.copyUpdateGroupInfoDtoToGroup(updateGroupInfoDto, existingGroup);

        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.groupToGroupInfoDto(updatedGroup);
    }

    @Override
    public GroupActiveMembersDto groupActiveMembersById(Long id) {
        groupMembershipService.verifyCurrentUserActiveMembership(id);

        Group group = groupModelById(id);

        List<AppUser> appUserList = groupMembershipRepository.queryActiveMembersOfGroupWithId(id);

        List<AppUserDto> appUserDtoList = appUserMapper.appUserListToAppUserDtoList(appUserList);

        GroupActiveMembersDto groupMembersDto = groupMapper.groupToGroupActiveMembersDto(group);
        groupMembersDto.setMembers(appUserDtoList);
        return groupMembersDto;
    }

    @Override
    public GroupMembershipDto groupMembership(Long id, UUID appUserId) {
        groupMembershipService.verifyCurrentUserActiveMembership(id);
        GroupMembership groupMembership = groupMembershipService.groupActiveMembershipModel(appUserId, id);
        return groupMembershipMapper.groupMembershipToGroupMembershipDto(groupMembership);
    }

    @Override
    public GroupInviteDto createGroupInvite(NewGroupInviteDto newGroupInviteDto) {
        Set<ConstraintViolation<NewGroupInviteDto>> violations = validator.validate(newGroupInviteDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Long groupId = newGroupInviteDto.getGroupId();
        UUID invitedAppUserId = newGroupInviteDto.getInvitedAppUserId();
        UUID currentAppUserId = identityService.currentUserID();

        if(invitedAppUserId.equals(currentAppUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot invite self in a group");
        }

        GroupMembership invitedByGroupMembership = groupMembershipService.groupActiveMembershipModel(currentAppUserId, groupId);

        if (groupMembershipService.isAppUserActiveMemberOfGroup(invitedAppUserId, groupId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invited user is already an active group member");
        }

        AppUser invitedAppUser = appUserService.appUserModelById(invitedAppUserId);

        Optional<GroupInvite> existingGroupInvite = groupInviteRepository.findByInvitedAppUserAndInvitedByGroupMembership(invitedAppUser, invitedByGroupMembership);
        if (existingGroupInvite.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already invited by the current user");
        }

        GroupInvite newGroupInvite = new GroupInvite();
        newGroupInvite.setInvitedAppUser(invitedAppUser);
        newGroupInvite.setInvitedByGroupMembership(invitedByGroupMembership);

        GroupInvite createdGroupInvite = groupInviteRepository.save(newGroupInvite);
        return groupInviteMapper.groupInviteToGroupInviteDto(createdGroupInvite);
    }
}
