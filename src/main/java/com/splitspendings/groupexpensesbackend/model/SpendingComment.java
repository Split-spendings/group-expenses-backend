package com.splitspendings.groupexpensesbackend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "spending_comment")
@Getter
@Setter
public class SpendingComment {

    public static final int MESSAGE_MIN_LENGTH = 1;
    public static final int MESSAGE_MAX_LENGTH = 250;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false, length = MESSAGE_MAX_LENGTH)
    private String message;

    @Column(name = "time_added", nullable = false)
    private ZonedDateTime timeAdded = ZonedDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spending_id", foreignKey = @ForeignKey(name = "fk_spending_comment_spending"))
    private Spending spending;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "added_by_app_user_id", foreignKey = @ForeignKey(name = "fk_spending_comment_added_by_user"))
    private AppUser addedByAppUser;
}
