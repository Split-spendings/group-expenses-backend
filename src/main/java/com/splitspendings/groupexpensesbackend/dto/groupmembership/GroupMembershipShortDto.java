package com.splitspendings.groupexpensesbackend.dto.groupmembership;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import lombok.Data;

@Data
public class GroupMembershipShortDto {

    private GroupInfoDto group;
    private AppUserDto appUser;
}
