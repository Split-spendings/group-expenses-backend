package com.splitspendings.groupexpensesbackend.dto.appuser;

import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import lombok.Data;

import java.util.List;

@Data
public class AppUserReceivedGroupInvitesDto {

    private List<GroupInviteDto> receivedGroupInvites;
}
