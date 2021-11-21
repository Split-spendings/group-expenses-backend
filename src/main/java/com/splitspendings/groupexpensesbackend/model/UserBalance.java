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

@Entity
@Table(name = "user_balance")
@Getter
@Setter
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_user_balance_group"))
    private Group group;

    // first appUser owes to second if balance is negative
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "first_app_user_id", foreignKey = @ForeignKey(name = "fk_user_balance_first_app_user"))
    private AppUser firstAppUser;

    // second appUser owes to first if balance is positive
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "second_app_user_id", foreignKey = @ForeignKey(name = "fk_user_balance_second_app_user"))
    private AppUser secondAppUser;
}