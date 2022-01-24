package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Spending spending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id", foreignKey = @ForeignKey(name = "fk_item_category"))
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "item")
    private Set<Share> shares;
}