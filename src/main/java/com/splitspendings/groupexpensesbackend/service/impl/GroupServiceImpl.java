package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.*;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.exception.InvalidGroupInviteException;
import com.splitspendings.groupexpensesbackend.mapper.*;
import com.splitspendings.groupexpensesbackend.model.*;
import com.splitspendings.groupexpensesbackend.repository.GroupInviteRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.ZonedDateTime;
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
    private final SpendingRepository spendingRepository;

    private final GroupMapper groupMapper;
    private final AppUserMapper appUserMapper;
    private final GroupMembershipMapper groupMembershipMapper;
    private final GroupInviteMapper groupInviteMapper;
    private final SpendingMapper spendingMapper;

    private final IdentityService identityService;
    private final AppUserService appUserService;
    private final GroupMembershipService groupMembershipService;
    private final GroupMembershipSettingsService groupMembershipSettingsService;

    @Override
    public Group groupModelById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
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

        GroupMembership createdGroupMembership = groupMembershipRepository.save(groupMembership);

        groupMembershipSettingsService.createAndSaveDefaultGroupMembershipSettingsForGroupMembership(createdGroupMembership);

        return groupMapper.groupToGroupInfoDto(createdGroup);
    }

    @Override
    public GroupInfoDto updateGroupInfo(Long id, UpdateGroupInfoDto updateGroupInfoDto) {
        updateGroupInfoDto.trim();

        Set<ConstraintViolation<UpdateGroupInfoDto>> violations = validator.validate(updateGroupInfoDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);

        Group existingGroup = groupModelById(id);

        existingGroup = groupMapper.copyUpdateGroupInfoDtoToGroup(updateGroupInfoDto, existingGroup);

        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.groupToGroupInfoDto(updatedGroup);
    }

    @Override
    public GroupActiveMembersDto groupActiveMembersById(Long id) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);

        Group group = groupModelById(id);

        List<AppUser> appUserList = groupMembershipRepository.queryActiveMembersOfGroupWithId(id);

        List<AppUserDto> appUserDtoList = appUserMapper.appUserListToAppUserDtoList(appUserList);

        GroupActiveMembersDto groupMembersDto = groupMapper.groupToGroupActiveMembersDto(group);
        groupMembersDto.setMembers(appUserDtoList);
        return groupMembersDto;
    }

    @Override
    public GroupMembershipDto groupMembership(Long id, UUID appUserId) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);
        GroupMembership groupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(appUserId, id);
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

        GroupMembership invitedByGroupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(currentAppUserId, groupId);

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

    @Override
    public GroupInvite groupInviteModelById(Long inviteId) {
        Optional<GroupInvite> groupInvite = groupInviteRepository.findById(inviteId);
        if (groupInvite.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group invite not found");
        }
        return groupInvite.get();
    }

    @Override
    @Transactional(noRollbackFor = InvalidGroupInviteException.class)
    public GroupInviteAcceptedDto acceptGroupInvite(Long inviteId) {
        GroupInvite groupInvite = groupInviteModelById(inviteId);

        AppUser invitedAppUser = groupInvite.getInvitedAppUser();
        identityService.verifyAuthorization(invitedAppUser.getId());

        GroupMembership invitedByGroupMembership = groupInvite.getInvitedByGroupMembership();

        if(!invitedByGroupMembership.getActive()) {
            groupInviteRepository.delete(groupInvite);
            throw new InvalidGroupInviteException(HttpStatus.BAD_REQUEST, "User who made an invite is no longer an active group member");
        }

        Group group = invitedByGroupMembership.getGroup();

        groupInviteRepository.deleteAllByInvitedByGroupMembershipGroupAndInvitedAppUser(group, invitedAppUser);

        Optional<GroupMembership> groupMembershipOptional = groupMembershipRepository.findByGroupAndAppUser(group, invitedAppUser);
        GroupMembership groupMembership;

        if(groupMembershipOptional.isPresent() && !groupMembershipOptional.get().getActive()) {
            groupMembership = groupMembershipOptional.get();
            groupMembership.setActive(true);
            groupMembership.setLastTimeJoined(ZonedDateTime.now());
            groupMembershipRepository.save(groupMembership);
        } else if(groupMembershipOptional.isEmpty()){
            groupMembership = new GroupMembership();
            groupMembership.setAppUser(invitedAppUser);
            groupMembership.setGroup(group);
            groupMembership.setActive(true);
            groupMembership.setHasAdminRights(false);
            groupMembership.setFirstTimeJoined(ZonedDateTime.now());
            groupMembership.setLastTimeJoined(ZonedDateTime.now());
            groupMembershipRepository.save(groupMembership);
            groupMembershipSettingsService.createAndSaveDefaultGroupMembershipSettingsForGroupMembership(groupMembership);
        } else {
            throw new InvalidGroupInviteException(HttpStatus.BAD_REQUEST, "Invited user is already an active member of a group");
        }

        GroupInviteAcceptedDto groupInviteAcceptedDto = new GroupInviteAcceptedDto();
        groupInviteAcceptedDto.setGroupMembership(groupMembershipMapper.groupMembershipToGroupMembershipDto(groupMembership));
        return groupInviteAcceptedDto;
    }

    @Override
    public void declineGroupInvite(Long id) {
        GroupInvite groupInvite = groupInviteModelById(id);
        identityService.verifyAuthorization(groupInvite.getInvitedAppUser().getId());
        groupInviteRepository.delete(groupInvite);
    }

    @Override
    public void leaveGroup(Long id) {
        Optional<GroupMembership> groupMembershipOptional = groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(id, identityService.currentUserID());
        if(groupMembershipOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a member of a group");
        }
        GroupMembership groupMembership = groupMembershipOptional.get();
        groupMembership.setActive(false);
        groupMembership.setLastTimeLeft(ZonedDateTime.now());
        groupMembershipRepository.save(groupMembership);
    }

    @Override
    public GroupSpendingsDto groupSpendings(Long id) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);

        Group group = groupModelById(id);

        List<Spending> spendingList = spendingRepository.findAllByAddedByGroupMembershipGroup(group);

        List<SpendingShortDto> spendingShortDtoList = spendingMapper.spendingListToSpendingShortDtoList(spendingList);

        GroupSpendingsDto groupSpendingsDto = groupMapper.groupToGroupSpendingsDto(group);
        groupSpendingsDto.setSpendings(spendingShortDtoList);
        return groupSpendingsDto;
    }
}
