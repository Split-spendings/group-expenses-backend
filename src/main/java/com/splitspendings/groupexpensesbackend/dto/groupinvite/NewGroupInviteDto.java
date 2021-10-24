package com.splitspendings.groupexpensesbackend.dto.groupinvite;

import com.splitspendings.groupexpensesbackend.model.GroupInvite;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class NewGroupInviteDto {

    @NotNull
    private Long groupId;

    @Size(max = GroupInvite.MESSAGE_MAX_LENGTH)
    private String message;

    @NotNull
    private UUID invitedAppUserId;

    public void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
