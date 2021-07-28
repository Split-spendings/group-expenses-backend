package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "spending")

@Getter
@Setter
public class Spending {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_membership_id", foreignKey = @ForeignKey(name = "fk_spending_membership"))
    private GroupMembership groupMembership;

    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "total", nullable = false)
    private BigDecimal totalSpending;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "receipt_photo")
    private byte[] receiptPhoto;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "time_payed")
    private ZonedDateTime timePayed = ZonedDateTime.now();
}
