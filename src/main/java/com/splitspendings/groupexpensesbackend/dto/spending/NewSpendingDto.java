package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.item.NewItemDto;
import com.splitspendings.groupexpensesbackend.model.Spending;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class NewSpendingDto {

    @NotNull
    private Long groupID;

    @NotNull
    @Size(min = Spending.TITLE_MIN_LENGTH, max = Spending.TITLE_MAX_LENGTH)
    private String title;

    private ZonedDateTime timePayed;

    private Currency currency;

    @NotNull
    private Long paidByGroupMembershipId;

    @NotEmpty
    private List<@Valid NewItemDto> newItemDtoList;

    public void setTitle(String title) {
        this.title = TrimUtil.trimAndRemoveExtraSpaces(title);
    }
}
