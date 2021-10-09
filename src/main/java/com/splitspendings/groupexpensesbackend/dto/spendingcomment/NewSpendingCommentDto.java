package com.splitspendings.groupexpensesbackend.dto.spendingcomment;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewSpendingCommentDto {
    @NotNull
    private Long spendingID;
    //todo
}
