package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "spending_limit")
@Getter
@Setter
public class SpendingLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "set_limit", nullable = false)
    private BigDecimal limit;

    @Column(name = "current_spending", nullable = false)
    private BigDecimal currentSpending;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "valid_from", nullable = false)
    private ZonedDateTime validFrom = ZonedDateTime.now();

    @Column(name = "valid_until")
    private ZonedDateTime validUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_spending_limit_group"))
    private Group group;
}
