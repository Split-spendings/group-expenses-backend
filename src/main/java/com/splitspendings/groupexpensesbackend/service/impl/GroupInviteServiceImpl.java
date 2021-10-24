package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.exception.InvalidGroupInviteException;
import com.splitspendings.groupexpensesbackend.mapper.GroupInviteMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.repository.GroupInviteRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupInviteService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupInviteServiceImpl implements GroupInviteService {

    private final Validator validator;

    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupInviteRepository groupInviteRepository;

    private final GroupMembershipMapper groupMembershipMapper;
    private final GroupInviteMapper groupInviteMapper;

    private final IdentityService identityService;
    private final AppUserService appUserService;
    private final GroupMembershipService groupMembershipService;
    private final GroupMembershipSettingsService groupMembershipSettingsService;

    /**
     * @param id
     *         id of {@link GroupInvite} to be found in the database
     *
     * @return {@link GroupInvite} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupInvite groupInviteModelById(Long id) {
        return groupInviteRepository.findById(id)
                .orElseThrow(() -> {
                    String logMessage = String.format("Group invite with id = {%d} not found", id);
                    log.info(logMessage);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, logMessage);
                });
    }

    /**
     * @param id
     *         id of {@link GroupInvite} to be found in the database
     *
     * @return {@link GroupInvite} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if {@link AppUser} has no right to access {@link
     *         GroupInvite}
     */
    @Override
    public GroupInviteDto groupInviteById(Long id) {
        GroupInvite groupInvite = groupInviteModelById(id);
        if (hasAccessToGroupInvite(groupInvite)) {
            return groupInviteMapper.groupInviteToGroupInviteDto(groupInvite);
        }
        String logMessage = String.format("User with id = {%s} has no right to access GroupInvite with id = {%d}",
                identityService.currentUserID(),
                id);
        log.info(logMessage);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, logMessage);
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
        newGroupInvite.setMessage(newGroupInviteDto.getMessage());

        groupInviteRepository.save(newGroupInvite);

        return groupInviteMapper.groupInviteToGroupInviteDto(newGroupInvite);
    }

    /**
     * @param id
     *         id of {@link GroupInvite} to be accepted
     *
     * @return {@link GroupInviteAcceptedDto} created from given {@link GroupInvite}
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if no {@link GroupInvite} with given id is found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} if {@link AppUser} is unauthorized to accept invitation
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if {@link AppUser} who created invitation is no longer an
     *         active member of a {@link Group}
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#BAD_REQUEST} if invited {@link AppUser} is an active member of a
     *         {@link Group}
     */
    @Override
    @Transactional(noRollbackFor = InvalidGroupInviteException.class)
    public GroupInviteAcceptedDto acceptGroupInvite(Long id) {
        GroupInvite groupInvite = groupInviteModelById(id);

        AppUser invitedAppUser = groupInvite.getInvitedAppUser();
        identityService.verifyAuthorization(invitedAppUser.getId());

        GroupMembership invitedByGroupMembership = groupInvite.getInvitedByGroupMembership();

        if (!invitedByGroupMembership.getActive()) {
            groupInviteRepository.delete(groupInvite);
            String logMessage =
                    String.format("AppUser with id = {%s} who made an invite with id = {%d} is no longer an active member of a group with id = {%d}",
                            invitedByGroupMembership.getAppUser().getId(),
                            id,
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

    private boolean hasAccessToGroupInvite(GroupInvite groupInvite) {
        UUID currentAppUserId = identityService.currentUserID();
        if (Objects.equals(currentAppUserId, groupInvite.getInvitedAppUser().getId())) {
            return true;
        }

        GroupMembership groupMembership = groupInvite.getInvitedByGroupMembership();
        if (Objects.equals(currentAppUserId, groupMembership.getAppUser().getId())
                && groupMembership.getActive()) {
            return true;
        }

        return groupMembershipService.isAdminOfGroup(currentAppUserId, groupMembership.getGroup().getId());
    }
}
