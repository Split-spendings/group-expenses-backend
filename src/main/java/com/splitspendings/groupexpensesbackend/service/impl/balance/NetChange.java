package com.splitspendings.groupexpensesbackend.service.impl.balance;

import com.splitspendings.groupexpensesbackend.model.AppUser;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 *  Net Change in Cash = (Sum of Cash Outflow - Sum of Cash Income) for given user
 */
@Data
@RequiredArgsConstructor
public class NetChange implements Comparable<NetChange>{
    private final AppUser appUser;

    /**
     * if positive then user has to receive the amount
     * if negative user must give away the amount
     */
    private BigDecimal amount = BigDecimal.ZERO;

    @Override
    public int compareTo(NetChange o) {
        return amount.compareTo(o.amount);
    }

    public void subtractAmount(BigDecimal sub){
        amount = amount.subtract(sub);
    }

    public void addAmount(BigDecimal add){
        amount = amount.add(add);
    }
}