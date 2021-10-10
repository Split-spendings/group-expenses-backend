package com.splitspendings.groupexpensesbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "spending_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingComment {

    public static final int MESSAGE_MIN_LENGTH = 1;
    public static final int MESSAGE_MAX_LENGTH = 250;

    public SpendingComment(String message, Spending spending, AppUser addedByAppUser) {
        this.message = message;
        this.spending = spending;
        this.addedByAppUser = addedByAppUser;
    }

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
