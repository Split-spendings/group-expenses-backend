package com.splitspendings.groupexpensesbackend.dto.share;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class NewShareDto {

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;

    @NotNull
    private Long paidForGroupMembershipId;
}
