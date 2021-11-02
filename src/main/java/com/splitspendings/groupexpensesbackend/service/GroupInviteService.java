package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.groupinvite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;

public interface GroupInviteService {

    GroupInvite groupInviteModelById(Long id);

    GroupInviteDto createGroupInvite(NewGroupInviteDto newGroupInviteDto);

    GroupInviteAcceptedDto acceptGroupInvite(Long id);

    void declineGroupInvite(Long id);
}
