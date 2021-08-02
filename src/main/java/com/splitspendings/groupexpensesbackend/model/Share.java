package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "share")

@Getter
@Setter
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by_user_id", foreignKey = @ForeignKey(name = "fk_share_app_user_1"))
    private AppUser paidBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_for_user_id", foreignKey = @ForeignKey(name = "fk_share_app_user_2"))
    private AppUser paidFor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_share_item"))
    private Item item;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();
}
