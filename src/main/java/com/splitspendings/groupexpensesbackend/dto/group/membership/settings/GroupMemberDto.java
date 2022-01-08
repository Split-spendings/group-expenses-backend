package com.splitspendings.groupexpensesbackend.dto.group.membership.settings;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import lombok.Data;

@Data
public class GroupMemberDto {

    private Long id;
    private Boolean active;
    private AppUserDto appUser;
}
