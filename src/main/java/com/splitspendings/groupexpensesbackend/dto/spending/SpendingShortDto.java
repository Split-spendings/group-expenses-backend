package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMemberDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class SpendingShortDto {

    private Long id;
    private String title;
    private BigDecimal totalAmount;
    private Currency currency;
    private ZonedDateTime timeCreated;
    private ZonedDateTime timePayed;
    private GroupMemberDto addedByGroupMembership;
    private GroupMemberDto paidByGroupMembership;
}
