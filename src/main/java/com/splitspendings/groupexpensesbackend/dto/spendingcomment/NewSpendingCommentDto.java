package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.model.SpendingComment;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewSpendingCommentDto {

    @NotNull
    private Long spendingId;

    @NotNull
    @Size(min = SpendingComment.MESSAGE_MIN_LENGTH, max = SpendingComment.MESSAGE_MAX_LENGTH)
    private String message;

    public void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
