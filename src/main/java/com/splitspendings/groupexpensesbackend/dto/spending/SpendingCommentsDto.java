package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.spending.comment.SpendingCommentShortDto;
import lombok.Data;

import java.util.List;

@Data
public class SpendingCommentsDto {

    private Long id;
    private String title;
    private List<SpendingCommentShortDto> comments;
}
