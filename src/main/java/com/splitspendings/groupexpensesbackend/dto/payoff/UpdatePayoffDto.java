package com.splitspendings.groupexpensesbackend.dto.payoff;

import com.splitspendings.groupexpensesbackend.model.Payoff;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.util.TrimUtil;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdatePayoffDto {
    @NotNull
    @Size(min = Payoff.TITLE_MIN_LENGTH, max = Payoff.TITLE_MAX_LENGTH)
    private String title;

    /**
     * amount of a Payoff to be saved, cannot be negative or zero
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private byte[] receiptPhoto;

    @NotNull
    private Currency currency;

    @NotNull
    private UUID paidForAppUser;

    @NotNull
    private UUID paidToAppUser;

    public void setTitle(String title) {
        this.title = TrimUtil.trimAndRemoveExtraSpaces(title);
    }
}