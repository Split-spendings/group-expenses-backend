package com.splitspendings.groupexpensesbackend.dto.group.membership;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class GroupMembershipDto {

    private Long id;
    private GroupDto group;
    private AppUserDto appUser;
    private Boolean active;
    private Boolean hasAdminRights;
    private ZonedDateTime timeCreated;
    private ZonedDateTime firstTimeJoined;
    private ZonedDateTime lastTimeJoined;
    private ZonedDateTime lastTimeLeft;
}
