package com.splitspendings.groupexpensesbackend.dto.spending;

import com.splitspendings.groupexpensesbackend.dto.group.membership.settings.GroupMemberDto;
import com.splitspendings.groupexpensesbackend.dto.share.ShareDto;
import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class SpendingDto {

    private Long id;
    private String title;
    private BigDecimal totalAmount;
    private Currency currency;
    private ZonedDateTime timeCreated;
    private ZonedDateTime timePayed;
    private GroupMemberDto addedByGroupMembership;
    private Set<ShareDto> shares;
}
