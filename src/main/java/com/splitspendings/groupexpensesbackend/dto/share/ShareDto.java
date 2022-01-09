package com.splitspendings.groupexpensesbackend.dto.share;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareDto {

    private BigDecimal amount;
    private AppUserDto paidForGroupMembershipId;
}
