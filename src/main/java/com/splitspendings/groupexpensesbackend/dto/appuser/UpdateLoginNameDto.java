package com.splitspendings.groupexpensesbackend.dto.appuser;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateLoginNameDto {

    @NotNull
    @Size(min = AppUser.NAMES_MIN_LENGTH, max = AppUser.NAMES_MAX_LENGTH)
    private String loginName;

    public void trim() {
        if (loginName != null) {
            loginName = TrimUtil.trimAndRemoveExtraSpaces(loginName);
        }
    }
}
