package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.spendingcomment.SpendingCommentShortDto;
import lombok.Data;

import java.util.Set;

@Data
public class SpendingCommentsDto {

    private Long id;
    private String title;
    private Set<SpendingCommentShortDto> comments;
}
