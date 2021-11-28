package com.splitspendings.groupexpensesbackend.service;

import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteAcceptedDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.GroupInviteDto;
import com.splitspendings.groupexpensesbackend.dto.group.invite.NewGroupInviteDto;
import com.splitspendings.groupexpensesbackend.model.GroupInvite;

public interface GroupInviteService {

    GroupInvite groupInviteModelById(Long id);

    GroupInviteDto groupInviteById(Long id);

    GroupInviteDto createGroupInvite(NewGroupInviteDto newGroupInviteDto);

    GroupInviteAcceptedDto acceptGroupInvite(Long id);

    void declineGroupInvite(Long id);
}
