package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewSpendingCommentDto {

    public NewSpendingCommentDto(Long spendingID, String message) {
        this.spendingID = spendingID;
        setMessage(message);
    }

    @NotNull
    private final Long spendingID;

    @NotNull
    private String message;

    private void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
