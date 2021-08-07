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

    @NotNull
    private Currency currency;

    @NotEmpty
    private List<@Valid NewItemDto> items;

    public void trim() {
        if(title != null) {
            title = TrimUtil.trimAndRemoveExtraSpaces(title);
        }
        if(items != null) {
            for(NewItemDto newItemDto: items) {
                newItemDto.trim();
            }
        }
    }
}
