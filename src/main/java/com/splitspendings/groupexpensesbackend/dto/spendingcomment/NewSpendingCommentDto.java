package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewSpendingCommentDto {

    public NewSpendingCommentDto(Long spendingID, UUID addedByAppUserId, String message) {
        this.spendingID = spendingID;
        this.addedByAppUserId = addedByAppUserId;
        setMessage(message);
    }

    @NotNull
    private final Long spendingID;

    @NotNull
    private final UUID addedByAppUserId;

    @NotNull
    private String message;

    private void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
