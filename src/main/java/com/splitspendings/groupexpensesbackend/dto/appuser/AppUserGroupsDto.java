package com.splitspendings.groupexpensesbackend.dto.appuser;

import com.splitspendings.groupexpensesbackend.dto.group.GroupInfoDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AppUserGroupsDto {

    private UUID id;
    private List<GroupInfoDto> groups;
}
