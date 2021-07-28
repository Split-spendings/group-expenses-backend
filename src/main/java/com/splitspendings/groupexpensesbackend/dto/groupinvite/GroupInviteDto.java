package com.splitspendings.groupexpensesbackend.dto.groupinvite;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipShortDto;
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
