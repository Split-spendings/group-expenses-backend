package com.splitspendings.groupexpensesbackend.dto.group;

import com.splitspendings.groupexpensesbackend.model.Group;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.InviteOption;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    private Currency defaultCurrency;

    public void setName(String name) {
        this.name = TrimUtil.trimAndRemoveExtraSpaces(name);
    }
}
