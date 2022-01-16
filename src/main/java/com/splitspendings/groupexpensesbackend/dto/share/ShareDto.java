package com.splitspendings.groupexpensesbackend.dto.share;

import com.splitspendings.groupexpensesbackend.dto.appuser.AppUserDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ShareDto {

    private Long id;
    private BigDecimal amount;
    private AppUserDto paidFor;
}
