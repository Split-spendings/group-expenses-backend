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

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by_app_user_id", foreignKey = @ForeignKey(name = "fk_share_paid_by_user"))
    private AppUser paidByAppUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_for_app_user_id", foreignKey = @ForeignKey(name = "fk_share_paid_for_user"))
    private AppUser paidForAppUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_share_item"))
    private Item item;
}
