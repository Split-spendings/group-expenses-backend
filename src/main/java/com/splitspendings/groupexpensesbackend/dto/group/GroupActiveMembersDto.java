package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import lombok.Data;

import java.util.List;

@Data
public class GroupActiveMembersDto {

    private Long id;
    private String name;
    private List<AppUserDto> members;
}
