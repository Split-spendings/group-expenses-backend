package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateGroupInfoDto {

    @NotNull
    @Size(min = Group.NAME_MIN_LENGTH, max = Group.NAME_MAX_LENGTH)
    private String name;

    public void trim() {
        if (name != null) {
            name = TrimUtil.trimAndRemoveExtraSpaces(name);
        }
    }
}
