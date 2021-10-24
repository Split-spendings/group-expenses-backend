package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupInfoDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.exception.InvalidGroupInviteException;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupInviteMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipMapper;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.repository.GroupInviteRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
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

    /**
     * @param id
     *         id of {@link Group} to be found in the database
     *
     * @return {@link Group} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public Group groupModelById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> {
                    String logMessage = String.format("Group with id = {%d} not found", id);
                    log.info(logMessage);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, logMessage);
                });
    }

    /**
     * @param id
     *         id of {@link Group} to be found in the database
     *
     * @return {@link GroupInfoDto} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupInfoDto groupInfoById(Long id) {
        return groupMapper.groupToGroupInfoDto(groupModelById(id));
    }

    /**
     * @param newGroupDto
     *         data to save new {@link Group}
     *
     * @return created {@link GroupInfoDto}
     *
     * @throws ConstraintViolationException
     *         if {@link NewGroupDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupInfoDto createGroup(NewGroupDto newGroupDto) {
        ValidatorUtil.validate(validator, newGroupDto);

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

    /**
     * @param id
     *         id of {@link Group} to be updated
     * @param updateGroupInfoDto
     *         data to update {@link Group} with
     *
     * @return {@link GroupInfoDto} with updated data
     *
     * @throws ConstraintViolationException
     *         if {@link UpdateGroupInfoDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if current user has no rights to update {@link Group}
     */
    @Override
    public GroupInfoDto updateGroupInfo(Long id, UpdateGroupInfoDto updateGroupInfoDto) {
        ValidatorUtil.validate(validator, updateGroupInfoDto);

        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);

        Group existingGroup = groupModelById(id);

        existingGroup = groupMapper.copyUpdateGroupInfoDtoToGroup(updateGroupInfoDto, existingGroup);

        Group updatedGroup = groupRepository.save(existingGroup);
        return groupMapper.groupToGroupInfoDto(updatedGroup);
    }

    /**
     * @param id
     *         id of {@link Group}
     *
     * @return all active members of {@link Group}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current user has no rights to access {@link Group}
     */
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

    /**
     * @param id
     *         id of {@link Group} to be found
     * @param appUserId
     *         id of {@link AppUser} to be found
     *
     * @return {@link GroupMembershipDto} with given {@link Group} and {@link AppUser}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current user has no rights to access {@link Group}
     */
    @Override
    public GroupMembershipDto groupMembership(Long id, UUID appUserId) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);
        GroupMembership groupMembership = groupMembershipService.groupActiveMembershipModelByGroupId(appUserId, id);
        return groupMembershipMapper.groupMembershipToGroupMembershipDto(groupMembership);
    }

    /**
     * @param newGroupInviteDto
     *         data to be used to create new {@link NewGroupInviteDto}
     *
     * @return created {@link GroupInviteDto}
     *
     * @throws ConstraintViolationException
     *         if {@link NewGroupInviteDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if user tries to invite itself
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if current user has no rights to access {@link Group}
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if invited {@link AppUser} is already an active group
     *         member
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if invited {@link AppUser} is already invited by current
     *         {@link AppUser}
     */
    @Override
    public GroupInviteDto createGroupInvite(NewGroupInviteDto newGroupInviteDto) {
        ValidatorUtil.validate(validator, newGroupInviteDto);

        Long groupId = newGroupInviteDto.getGroupId();
        UUID invitedAppUserId = newGroupInviteDto.getInvitedAppUserId();
        UUID currentAppUserId = identityService.currentUserID();

        if (invitedAppUserId.equals(currentAppUserId)) {
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

    /**
     * @param inviteId
     *         id of {@link GroupInvite} to be found in the database
     *
     * @return {@link GroupInvite} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupInvite groupInviteModelById(Long inviteId) {
        return groupInviteRepository.findById(inviteId)
                .orElseThrow(() -> {
                    String logMessage = String.format("Group invite with id = {%d} not found", inviteId);
                    log.info(logMessage);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, logMessage);
                });
    }

    /**
     * @param inviteId
     *         id of {@link GroupInvite} to be accepted
     *
     * @return {@link GroupInviteAcceptedDto} created from given {@link GroupInvite}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if no {@link GroupInvite} with given id is found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if {@link AppUser} is unathorized to accept invitation
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if {@link AppUser} who created invitation is no longer an
     *         active member of a {@link Group}
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if invited {@link AppUser} is an active member of a
     *         {@link Group}
     */
    @Override
    @Transactional(noRollbackFor = InvalidGroupInviteException.class)
    public GroupInviteAcceptedDto acceptGroupInvite(Long inviteId) {
        GroupInvite groupInvite = groupInviteModelById(inviteId);

        AppUser invitedAppUser = groupInvite.getInvitedAppUser();
        identityService.verifyAuthorization(invitedAppUser.getId());

        GroupMembership invitedByGroupMembership = groupInvite.getInvitedByGroupMembership();

        if (!invitedByGroupMembership.getActive()) {
            groupInviteRepository.delete(groupInvite);
            String logMessage =
                    String.format("AppUser with id = {%s} who made an invite with id = {%d} is no longer an active member of a group with id = {%d}",
                            invitedByGroupMembership.getAppUser().getId(),
                            inviteId,
                            invitedByGroupMembership.getGroup().getId());
            log.info(logMessage);
            throw new InvalidGroupInviteException(HttpStatus.BAD_REQUEST, logMessage);
        }

        Group group = invitedByGroupMembership.getGroup();

        groupInviteRepository.deleteAllByInvitedByGroupMembershipGroupAndInvitedAppUser(group, invitedAppUser);

        Optional<GroupMembership> groupMembershipOptional = groupMembershipRepository.findByGroupAndAppUser(group, invitedAppUser);
        GroupMembership groupMembership;

        if (groupMembershipOptional.isPresent() && !groupMembershipOptional.get().getActive()) {
            groupMembership = groupMembershipOptional.get();
            groupMembership.setActive(true);
            groupMembership.setLastTimeJoined(ZonedDateTime.now());
            groupMembershipRepository.save(groupMembership);
        } else if (groupMembershipOptional.isEmpty()) {
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
            String logMessage = String.format("Invited user with id = {%s} is already an active member of a group with id = {%d}",
                    invitedByGroupMembership.getAppUser().getId(), group.getId());
            log.info(logMessage);
            throw new InvalidGroupInviteException(HttpStatus.BAD_REQUEST, logMessage);
        }

        GroupInviteAcceptedDto groupInviteAcceptedDto = new GroupInviteAcceptedDto();
        groupInviteAcceptedDto.setGroupMembership(groupMembershipMapper.groupMembershipToGroupMembershipDto(groupMembership));
        return groupInviteAcceptedDto;
    }

    /**
     * Deletes {@link GroupInvite} with given id
     *
     * @param id
     *         id of a {@link GroupInvite} to be deleted
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if {@link GroupInvite} with given id is not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if {@link AppUser} who created invitation is no longer an
     *         active member of a {@link Group}
     */
    @Override
    public void declineGroupInvite(Long id) {
        GroupInvite groupInvite = groupInviteModelById(id);
        identityService.verifyAuthorization(groupInvite.getInvitedAppUser().getId());
        groupInviteRepository.delete(groupInvite);
    }

    /**
     * @param id
     *         id of a {@link Group} to leave
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} if there is no {@link Group} with given id
     */
    @Override
    public void leaveGroup(Long id) {
        GroupMembership groupMembership = groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(id, identityService.currentUserID())
                .orElseThrow(() -> {
                    String logMessage = String.format("User with id = {%s} is not a member of a group with id = {%d}",
                            identityService.currentUserID(), id);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, logMessage);
                });
        groupMembership.setActive(false);
        groupMembership.setLastTimeLeft(ZonedDateTime.now());
        groupMembershipRepository.save(groupMembership);
    }

    /**
     * @param id
     *         id of a {@link Group} to find {@link Spending}
     *
     * @return all {@link Spending} from given {@link Group}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if {@link AppUser} is not an active member of {@link
     *         Group}
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} when {@link Group} eith given id is not found
     */
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
