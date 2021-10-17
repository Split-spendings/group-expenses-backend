package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "payoff")
@Getter
@Setter
public class Payoff {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "receipt_photo")
    private byte[] receiptPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_payoff_group"))
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "added_by_app_user_id", foreignKey = @ForeignKey(name = "fk_payoff_added_by_app_user"))
    private AppUser addedByAppUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_for_app_user_id", foreignKey = @ForeignKey(name = "fk_payoff_paid_for_app_user"))
    private AppUser paidForAppUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_to_app_user_id", foreignKey = @ForeignKey(name = "fk_payoff_paid_to_app_user"))
    private AppUser paidToAppUser;
}
