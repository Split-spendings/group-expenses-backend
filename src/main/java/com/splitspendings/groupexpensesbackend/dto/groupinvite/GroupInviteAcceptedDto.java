package com.splitspendings.groupexpensesbackend.dto.groupinvite;

import com.splitspendings.groupexpensesbackend.dto.groupmembership.GroupMembershipDto;
import lombok.Data;

@Data
public class GroupInviteAcceptedDto {

    private GroupMembershipDto groupMembership;
}
