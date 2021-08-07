package com.splitspendings.groupexpensesbackend.dto.item;

import com.splitspendings.groupexpensesbackend.model.Item;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class NewItemDto {

    @NotNull
    @Size(min = Item.TITLE_MIN_LENGTH, max = Item.TITLE_MAX_LENGTH)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    public void trim() {
        if(title != null) {
            title = TrimUtil.trimAndRemoveExtraSpaces(title);
        }
    }
}
