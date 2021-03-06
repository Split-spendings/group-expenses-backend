package com.splitspendings.groupexpensesbackend.model;

import com.splitspendings.groupexpensesbackend.model.enums.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "spending")
@Getter
@Setter
public class Spending {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "receipt_photo")
    private byte[] receiptPhoto;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @Column(name = "time_payed")
    private ZonedDateTime timePayed = ZonedDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false, length = Currency.MAX_LENGTH)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "added_by_group_membership_id", foreignKey = @ForeignKey(name = "fk_spending_added_by_membership"))
    private GroupMembership addedByGroupMembership;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by_group_membership_id", foreignKey = @ForeignKey(name = "fk_spending_paid_by_membership"))
    private GroupMembership paidByGroupMembership;

    @OneToMany(mappedBy = "spending")
    private Set<SpendingComment> comments;

    @OneToMany(mappedBy = "spending")
    private Set<Item> items;
}
