package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import com.splitspendings.groupexpensesbackend.model.enums.InviteOption;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "user_group")
@Getter
@Setter
public class Group {
    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(name = "simplify_debts", nullable = false)
    private Boolean simplifyDebts = false;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "last_time_opened", nullable = false)
    private ZonedDateTime lastTimeOpened = ZonedDateTime.now();

    @Column(name = "last_time_closed")
    private ZonedDateTime lastTimeClosed;

    @Column(name = "personal", nullable = false)
    private Boolean personal;

    @Enumerated(EnumType.STRING)
    @Column(name = "invite_option", nullable = false, length = InviteOption.MAX_LENGTH)
    private InviteOption inviteOption;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_group_owner"))
    private AppUser owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency defaultCurrency;

    @OneToMany(mappedBy = "group")
    private Set<GroupMembership> groupMemberships;

    @OneToMany(mappedBy = "group")
    private Set<ItemCategory> createdCategories;

    @OneToMany(mappedBy = "group")
    private Set<SpendingLimit> spendingLimits;

    @OneToMany(mappedBy = "group")
    private Set<UserBalance> userBalances;

    @OneToMany(mappedBy = "group")
    private Set<Payoff> payoffs;
}
