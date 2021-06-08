package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.enums.InviteOption;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class NewGroupDto {

    @NotNull
    @Size(min = Group.NAME_MIN_LENGTH, max = Group.NAME_MAX_LENGTH)
    private String name;

    @NotNull
    private Boolean personal;

    @NotNull
    private InviteOption inviteOption;

    @NotNull
    private UUID ownerId;

    public void trim() {
        if(name != null) {
            name = name.trim();
        }
    }
}
