package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMemberDto;
import java.util.List;
import lombok.Data;

@Data
public class GroupMembersDto {

    private Long id;
    private String name;
    private List<GroupMemberDto> members;
}
