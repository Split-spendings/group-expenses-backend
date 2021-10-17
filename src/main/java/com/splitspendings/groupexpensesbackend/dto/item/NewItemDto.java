package com.splitspendings.groupexpensesbackend.dto.item;

import com.splitspendings.groupexpensesbackend.dto.share.NewShareDto;
import com.splitspendings.groupexpensesbackend.model.Item;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewItemDto {

    @NotNull
    @Size(min = Item.TITLE_MIN_LENGTH, max = Item.TITLE_MAX_LENGTH)
    private String title;

    @NotEmpty
    private List<@Valid NewShareDto> newShareDtoList;

    public void setTitle(String title) {
        this.title = TrimUtil.trimAndRemoveExtraSpaces(title);
    }
}
