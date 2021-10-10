package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewSpendingCommentDto {

    public NewSpendingCommentDto(Long spendingID, Long addedByAppUserId, String message) {
        this.spendingID = spendingID;
        this.addedByAppUserId = addedByAppUserId;
        setMessage(message);
    }

    @NotNull
    private final Long spendingID;

    @NotNull
    private final Long addedByAppUserId;

    @NotNull
    private String message;

    private void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
