package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {

    public static final int TITLE_MIN_LENGTH = 1;
    public static final int TITLE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "time_created", nullable = false)
    private ZonedDateTime timeCreated = ZonedDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spending_id", foreignKey = @ForeignKey(name = "fk_item_spending"))
    private Spending spending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id", foreignKey = @ForeignKey(name = "fk_item_category"))
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "item")
    private Set<Share> shares;
}