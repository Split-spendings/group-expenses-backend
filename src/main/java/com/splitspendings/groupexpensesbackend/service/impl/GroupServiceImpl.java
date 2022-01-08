package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.group.GroupActiveMembersDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupSpendingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.NewGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.UpdateGroupDto;
import com.splitspendings.groupexpensesbackend.dto.group.enums.GroupFilter;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMemberDto;
import com.splitspendings.groupexpensesbackend.dto.spending.SpendingShortDto;
import com.splitspendings.groupexpensesbackend.mapper.AppUserMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMapper;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipMapper;
import com.splitspendings.groupexpensesbackend.mapper.SpendingMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipRepository;
import com.splitspendings.groupexpensesbackend.repository.GroupRepository;
import com.splitspendings.groupexpensesbackend.repository.SpendingRepository;
import com.splitspendings.groupexpensesbackend.service.AppUserBalanceService;
import com.splitspendings.groupexpensesbackend.service.AppUserService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import com.splitspendings.groupexpensesbackend.service.GroupService;
import com.splitspendings.groupexpensesbackend.service.IdentityService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final Validator validator;

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final SpendingRepository spendingRepository;

    private final GroupMapper groupMapper;
    private final AppUserMapper appUserMapper;
    private final GroupMembershipMapper groupMembershipMapper;
    private final SpendingMapper spendingMapper;

    private final IdentityService identityService;
    private final AppUserService appUserService;
    private final AppUserBalanceService appUserBalanceService;
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
        return groupRepository.findById(id).orElseThrow(() ->
                LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("Group with id = {%d} not found", id)));
    }

    /**
     * @param id
     *         id of {@link Group} to be found in the database
     *
     * @return {@link GroupDto} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupDto groupById(Long id) {
        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);
        return groupMapper.groupToGroupInfoDto(groupModelById(id));
    }

    /**
     * @param newGroupDto
     *         data to save new {@link Group}
     *
     * @return created {@link GroupDto}
     *
     * @throws ConstraintViolationException
     *         if {@link NewGroupDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     */
    @Override
    public GroupDto createGroup(NewGroupDto newGroupDto) {
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
     * @param updateGroupDto
     *         data to update {@link Group} with
     *
     * @return {@link GroupDto} with updated data
     *
     * @throws ConstraintViolationException
     *         if {@link UpdateGroupDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if not found
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} if current user has no rights to update {@link Group}
     */
    @Override
    public GroupDto updateGroup(Long id, UpdateGroupDto updateGroupDto) {
        ValidatorUtil.validate(validator, updateGroupDto);

        Group group = groupModelById(id);
        boolean recalculateUserBalances = !group.getSimplifyDebts().equals(updateGroupDto.getSimplifyDebts());

        groupMembershipService.verifyCurrentUserActiveMembershipByGroupId(id);
        groupMapper.copyUpdateGroupInfoDtoToGroup(updateGroupDto, group);
        groupRepository.save(group);

        if (recalculateUserBalances){
            appUserBalanceService.recalculateAppUserBalanceByGroup(group);
        }

        return groupMapper.groupToGroupInfoDto(group);
    }

    /**
     * @param groupFilter
     *          used to filter groups
     * @return {@link List} of {@link GroupDto} filtered by {@link GroupFilter}
     */
    @Override
    public List<GroupDto> getAllGroupsFilterBy(GroupFilter groupFilter) {
        UUID appUserId = identityService.currentUserID();
        switch (groupFilter){
            case ALL:
                return groupMapper.groupMembershipListToGroupInfoDtoList(groupMembershipRepository.findAllByAppUserId(appUserId));
            case CURRENT:
                return groupMapper.groupMembershipListToGroupInfoDtoList(groupMembershipRepository.findAllByAppUserIdAndIsActive(appUserId, true));
            case FORMER:
                return groupMapper.groupMembershipListToGroupInfoDtoList(groupMembershipRepository.findAllByAppUserIdAndIsActive(appUserId, false));
            default:
                throw new IllegalStateException();
        }
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

        List<GroupMembership> groupMembers = groupMembershipRepository.getActiveMembersOfGroupWithId(id);

        List<GroupMemberDto> groupMemberDtoList = groupMembershipMapper.groupMembershipListToGroupMemberDtoList(groupMembers);

        GroupActiveMembersDto groupMembersDto = groupMapper.groupToGroupActiveMembersDto(group);
        groupMembersDto.setMembers(groupMemberDtoList);
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
     * @param id
     *         id of a {@link Group} to leave
     *
     * @throws ResponseStatusException
     *         with status {@link HttpStatus#NOT_FOUND} if there is no {@link Group} with given id
     */
    @Override
    public void leaveGroup(Long id) {
        GroupMembership groupMembership = groupMembershipRepository.queryByGroupIdAndAppUserIdAndActiveTrue(id, identityService.currentUserID())
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.FORBIDDEN,
                        String.format("User with id = {%s} is not a member of a group with id = {%d}",
                                identityService.currentUserID(),
                                id)));
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
     *         with status code {@link HttpStatus#NOT_FOUND} when {@link Group} with given id is not found
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
