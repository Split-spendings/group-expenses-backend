package com.splitspendings.groupexpensesbackend.dto.group.invite;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipShortDto;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GroupInviteDto {

    private Long id;
    private String message;
    private ZonedDateTime timeCreated;
    private AppUserDto invitedAppUser;
    private GroupMembershipShortDto invitedByGroupMembership;
}
