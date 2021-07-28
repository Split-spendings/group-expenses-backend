package com.splitspendings.groupexpensesbackend.dto.groupinvite;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewGroupInviteDto {

    @NotNull
    private Long groupId;

    @NotNull
    private UUID invitedAppUserId;
}
