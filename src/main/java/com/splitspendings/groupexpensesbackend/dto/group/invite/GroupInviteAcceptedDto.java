package com.splitspendings.groupexpensesbackend.dto.group.invite;

import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipDto;
import lombok.Data;

@Data
public class GroupInviteAcceptedDto {

    private GroupMembershipDto groupMembership;
}
