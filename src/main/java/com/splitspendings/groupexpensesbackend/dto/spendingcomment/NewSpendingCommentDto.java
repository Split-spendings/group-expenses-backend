package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewSpendingCommentDto {

    @NotNull
    private Long spendingID;

    @NotNull
    private String message;

    public void setMessage(String message){
        this.message = TrimUtil.trimAndRemoveExtraSpaces(message);
    }
}
