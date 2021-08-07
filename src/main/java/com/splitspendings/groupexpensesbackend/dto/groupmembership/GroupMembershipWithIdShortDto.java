package com.splitspendings.groupexpensesbackend.dto.groupmembership;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import lombok.Data;

@Data
public class GroupMembershipWithIdShortDto {

    private Long id;
    private Boolean active;
    private GroupInfoDto group;
    private AppUserDto appUser;
}
