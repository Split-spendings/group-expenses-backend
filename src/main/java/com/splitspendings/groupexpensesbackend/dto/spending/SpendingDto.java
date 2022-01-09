package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.group.membership.GroupMembershipWithIdShortDto;
import com.splitspendings.groupexpensesbackend.dto.share.ShareDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
public class SpendingDto {

    private Long id;
    private String title;
    private BigDecimal totalAmount;
    private ZonedDateTime timeCreated;
    private ZonedDateTime timePayed;
    private Currency currency;
    private GroupMembershipWithIdShortDto addedByGroupMembership;
    private Set<ShareDto> shareDtoSet;
}
