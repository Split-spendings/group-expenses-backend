package com.splitspendings.groupexpensesbackend.dto.group.membership;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import com.splitspendings.groupexpensesbackend.dto.group.GroupDto;
import lombok.Data;

@Data
public class GroupMembershipWithIdShortDto {

    private Long id;
    private Boolean active;
    private GroupDto group;
    private AppUserDto appUser;
}
