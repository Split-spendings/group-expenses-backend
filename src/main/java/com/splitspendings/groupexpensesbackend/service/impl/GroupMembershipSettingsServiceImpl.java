package com.splitspendings.groupexpensesbackend.service.impl;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.UpdateGroupMembershipSettingsDto;
import com.splitspendings.groupexpensesbackend.mapper.GroupMembershipSettingsMapper;
import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.model.GroupMembership;
import com.splitspendings.groupexpensesbackend.model.GroupMembershipSettings;
import com.splitspendings.groupexpensesbackend.repository.GroupMembershipSettingsRepository;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipService;
import com.splitspendings.groupexpensesbackend.service.GroupMembershipSettingsService;
import com.splitspendings.groupexpensesbackend.util.LogUtil;
import com.splitspendings.groupexpensesbackend.util.ValidatorUtil;
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
public class GroupMembershipSettingsServiceImpl implements GroupMembershipSettingsService {

    private final Validator validator;

    private final GroupMembershipSettingsRepository groupMembershipSettingsRepository;

    private final GroupMembershipSettingsMapper groupMembershipSettingsMapper;

    private final GroupMembershipService groupMembershipService;

    /**
     * @param id
     *         id of {@link GroupMembershipSettings} to be found in the database
     *
     * @return {@link GroupMembershipSettings} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} when there is no {@link GroupMembershipSettings} with given
     *         id
     */
    @Override
    public GroupMembershipSettings groupMembershipSettingsModelById(Long id) {
        return groupMembershipSettingsRepository.findById(id)
                .orElseThrow(() -> LogUtil.logMessageAndReturnResponseStatusException(log, HttpStatus.NOT_FOUND,
                        String.format("GroupMembershipSettings with id = {%d} not found", id)));
    }

    /**
     * @param id
     *         id of {@link GroupMembershipSettings} to be found in the database
     *
     * @return {@link GroupMembershipSettingsDto} with given id
     *
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} when there is no {@link GroupMembershipSettings} with given
     *         id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} when current {@link AppUser} has no rights to access {@link
     *         GroupMembershipSettings} with given id
     */
    @Override
    public GroupMembershipSettingsDto groupMembershipSettingsById(Long id) {
        groupMembershipService.verifyCurrentUserActiveMembershipById(id);
        return groupMembershipSettingsMapper.groupMembershipSettingsToGroupMembershipSettingsDto(groupMembershipSettingsModelById(id));
    }

    /**
     * @param id
     *         id of {@link GroupMembershipSettings} to be updated
     * @param updateGroupMembershipSettingsDto
     *         data to be updated
     *
     * @return {@link GroupMembershipSettingsDto} with updated data
     *
     * @throws ConstraintViolationException
     *         if given {@link UpdateGroupMembershipSettingsDto} is not valid
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#FORBIDDEN} when current {@link AppUser} has no rights to access {@link
     *         GroupMembership} with given id
     * @throws ResponseStatusException
     *         with status code {@link HttpStatus#NOT_FOUND} when there is no {@link GroupMembershipSettings} with given
     *         id
     */
    @Override
    public GroupMembershipSettingsDto updateGroupMembershipSettings(Long id, UpdateGroupMembershipSettingsDto updateGroupMembershipSettingsDto) {
        ValidatorUtil.validate(validator, updateGroupMembershipSettingsDto);
        groupMembershipService.verifyCurrentUserActiveMembershipById(id);

        GroupMembershipSettings existingGroupMembershipSettings = groupMembershipSettingsModelById(id);
        GroupMembershipSettings newGroupMembershipSettings = groupMembershipSettingsMapper.copyUpdateGroupMembershipSettingsDtoToGroupMembershipSettings(updateGroupMembershipSettingsDto, existingGroupMembershipSettings);
        GroupMembershipSettings updatedGroupMembershipSettings = groupMembershipSettingsRepository.save(newGroupMembershipSettings);
        return groupMembershipSettingsMapper.groupMembershipSettingsToGroupMembershipSettingsDto(updatedGroupMembershipSettings);
    }
}